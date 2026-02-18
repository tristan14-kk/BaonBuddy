package com.mobdev.baonbuddy.ui.transactions

import android.app.DatePickerDialog
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
import com.mobdev.baonbuddy.data.database.entity.GoalEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SetGoalFragment : Fragment() {

    private var selectedDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageView>(R.id.backButton)
        val goalNameInput = view.findViewById<EditText>(R.id.goalNameInput)
        val targetAmountInput = view.findViewById<EditText>(R.id.targetAmountInput)
        val targetDateInput = view.findViewById<EditText>(R.id.targetDateInput)
        val createGoalButton = view.findViewById<Button>(R.id.createGoalButton)

        backButton.setOnClickListener { findNavController().navigateUp() }

        targetDateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    selectedDate = dateFormat.format(calendar.time)
                    targetDateInput.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.minDate = System.currentTimeMillis()
            }.show()
        }

        createGoalButton.setOnClickListener {
            val goalName = goalNameInput.text.toString().trim()
            val targetAmountStr = targetAmountInput.text.toString()

            if (goalName.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a goal name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (targetAmountStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a target amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val targetAmount = targetAmountStr.toDoubleOrNull()
            if (targetAmount == null || targetAmount <= 0) {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repository = (requireActivity().application as BaonBuddyApplication).repository

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val goal = GoalEntity(
                    name = goalName,
                    targetAmount = targetAmount,
                    targetDate = selectedDate
                )
                repository.insertGoal(goal)

                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Goal created!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }
}