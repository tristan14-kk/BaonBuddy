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

class AddMoneyFragment : Fragment() {

    private var selectedSource = "Parents"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val amountInput = view.findViewById<EditText>(R.id.amountInput)
        val btnParents = view.findViewById<Button>(R.id.btnParents)
        val btnAllowance = view.findViewById<Button>(R.id.btnAllowance)
        val btnGift = view.findViewById<Button>(R.id.btnGift)
        val btnOther = view.findViewById<Button>(R.id.btnOther)
        val btn50 = view.findViewById<Button>(R.id.btn50)
        val btn100 = view.findViewById<Button>(R.id.btn100)
        val btn200 = view.findViewById<Button>(R.id.btn200)
        val addButton = view.findViewById<Button>(R.id.addButton)

        val sourceButtons = listOf(btnParents, btnAllowance, btnGift, btnOther)

        backButton.setOnClickListener { findNavController().navigateUp() }

        btn50.setOnClickListener { amountInput.setText("50") }
        btn100.setOnClickListener { amountInput.setText("100") }
        btn200.setOnClickListener { amountInput.setText("200") }

        fun selectSource(selected: Button, source: String) {
            selectedSource = source
            sourceButtons.forEach { btn ->
                if (btn == selected) {
                    btn.setBackgroundResource(R.drawable.button_primary)
                    btn.setTextColor(requireContext().getColor(R.color.white))
                } else {
                    btn.setBackgroundResource(R.drawable.button_outline)
                    btn.setTextColor(requireContext().getColor(R.color.navy_blue))
                }
            }
        }

        btnParents.setOnClickListener { selectSource(btnParents, "Parents") }
        btnAllowance.setOnClickListener { selectSource(btnAllowance, "Allowance") }
        btnGift.setOnClickListener { selectSource(btnGift, "Gift") }
        btnOther.setOnClickListener { selectSource(btnOther, "Other") }

        addButton.setOnClickListener {
            val amountStr = amountInput.text.toString()

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
                    title = "Added from $selectedSource",
                    amount = amount,
                    isIncome = true,
                    category = selectedSource
                )
                repository.insertTransaction(transaction)
                repository.addToBalance(amount)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "₱${String.format("%.2f", amount)} added!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}