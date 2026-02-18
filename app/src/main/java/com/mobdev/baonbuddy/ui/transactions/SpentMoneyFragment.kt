package com.mobdev.baonbuddy.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.database.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SpentMoneyFragment : Fragment() {

    private var selectedCategory = "Food"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_spent_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val amountInput = view.findViewById<EditText>(R.id.amountInput)
        val descriptionInput = view.findViewById<EditText>(R.id.descriptionInput)
        val btnFood = view.findViewById<Button>(R.id.btnFood)
        val btnTransport = view.findViewById<Button>(R.id.btnTransport)
        val btnSchool = view.findViewById<Button>(R.id.btnSchool)
        val btnOther = view.findViewById<Button>(R.id.btnOther)
        val spentButton = view.findViewById<Button>(R.id.spentButton)

        val categoryButtons = listOf(btnFood, btnTransport, btnSchool, btnOther)

        backButton.setOnClickListener { findNavController().navigateUp() }

        fun selectCategory(selected: Button, category: String) {
            selectedCategory = category
            categoryButtons.forEach { btn ->
                if (btn == selected) {
                    btn.setBackgroundResource(R.drawable.button_primary)
                    btn.setTextColor(requireContext().getColor(R.color.white))
                } else {
                    btn.setBackgroundResource(R.drawable.button_outline)
                    btn.setTextColor(requireContext().getColor(R.color.navy_blue))
                }
            }
        }

        btnFood.setOnClickListener { selectCategory(btnFood, "Food") }
        btnTransport.setOnClickListener { selectCategory(btnTransport, "Transport") }
        btnSchool.setOnClickListener { selectCategory(btnSchool, "School") }
        btnOther.setOnClickListener { selectCategory(btnOther, "Other") }

        spentButton.setOnClickListener {
            val amountStr = amountInput.text.toString()
            val description = descriptionInput.text.toString().ifEmpty { selectedCategory }

            if (amountStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = (requireActivity().application as BaonBuddyApplication).repository

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val transaction = TransactionEntity(
                    title = description,
                    amount = amount,
                    isIncome = false,
                    category = selectedCategory
                )
                repository.insertTransaction(transaction)
                repository.subtractFromBalance(amount)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "₱${String.format("%.2f", amount)} spent!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}