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
import java.text.NumberFormat
import java.util.*

class AddToGoalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_to_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val goalNameText = view.findViewById<TextView>(R.id.goalNameText)
        val progressText = view.findViewById<TextView>(R.id.progressText)
        val goalProgress = view.findViewById<ProgressBar>(R.id.goalProgress)
        val amountInput = view.findViewById<EditText>(R.id.amountInput)
        val addButton = view.findViewById<Button>(R.id.addButton)

        val goalId = arguments?.getInt("goalId") ?: -1
        val goalName = arguments?.getString("goalName") ?: "Goal"
        val goalTarget = arguments?.getDouble("goalTarget") ?: 0.0
        val goalSaved = arguments?.getDouble("goalSaved") ?: 0.0

        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0

        goalNameText.text = goalName
        progressText.text = "₱${formatter.format(goalSaved)} / ₱${formatter.format(goalTarget)}"

        val progress = if (goalTarget > 0) ((goalSaved / goalTarget) * 100).toInt() else 0
        goalProgress?.progress = progress.coerceIn(0, 100)

        backButton.setOnClickListener { findNavController().navigateUp() }

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

            if (goalId == -1) {
                Toast.makeText(requireContext(), "Invalid goal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save to Room Database
            val repository = (requireActivity().application as BaonBuddyApplication).repository

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                repository.addToGoal(goalId, amount)
                repository.subtractFromBalance(amount)

                val transaction = TransactionEntity(
                    title = "Added to: $goalName",
                    amount = amount,
                    isIncome = false,
                    category = "Goal"
                )
                repository.insertTransaction(transaction)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "₱${String.format("%.2f", amount)} added to goal!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}