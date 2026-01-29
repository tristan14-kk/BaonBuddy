package com.mobdev.baonbuddy.ui.goals

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdev.baonbuddy.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SetGoalActivity : AppCompatActivity() {

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goal)

        // Get views
        val backButton = findViewById<ImageView>(R.id.backButton)
        val goalNameInput = findViewById<EditText>(R.id.goalNameInput)
        val targetAmountInput = findViewById<EditText>(R.id.targetAmountInput)
        val targetDateInput = findViewById<EditText>(R.id.targetDateInput)
        val calendarIcon = findViewById<ImageView>(R.id.calendarIcon)
        val createGoalButton = findViewById<Button>(R.id.createGoalButton)

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Date picker
        val dateClickListener = {
            showDatePicker(targetDateInput)
        }
        targetDateInput.setOnClickListener { dateClickListener() }
        calendarIcon.setOnClickListener { dateClickListener() }

        // Create Goal button
        createGoalButton.setOnClickListener {
            val goalName = goalNameInput.text.toString().trim()
            val targetAmountText = targetAmountInput.text.toString().trim()

            if (goalName.isEmpty()) {
                Toast.makeText(this, "Please enter a goal name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (targetAmountText.isEmpty()) {
                Toast.makeText(this, "Please enter a target amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val targetAmount = targetAmountText.toDoubleOrNull()
            if (targetAmount == null || targetAmount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the goal
            saveGoal(goalName, targetAmount)
        }
    }

    private fun showDatePicker(targetDateInput: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectedCalendar.time)
                targetDateInput.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun saveGoal(goalName: String, targetAmount: Double) {
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Save goal to list
        val goalsJson = sharedPref.getString("goals", "[]") ?: "[]"
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val createdDate = dateFormat.format(java.util.Date())

        // Create goal JSON
        val newGoal = """{"name":"$goalName","targetAmount":$targetAmount,"savedAmount":0,"targetDate":"$selectedDate","createdDate":"$createdDate"}"""
        val updatedJson = if (goalsJson == "[]") {
            "[$newGoal]"
        } else {
            "[$newGoal,${goalsJson.substring(1)}"
        }

        editor.putString("goals", updatedJson)
        editor.apply()

        // Also add to transactions
        val transactionsJson = sharedPref.getString("transactions", "[]") ?: "[]"
        val newTransaction = """{"title":"New Goal: $goalName","date":"$createdDate","amount":$targetAmount,"isIncome":true}"""
        val updatedTransactions = if (transactionsJson == "[]") {
            "[$newTransaction]"
        } else {
            "[$newTransaction,${transactionsJson.substring(1)}"
        }
        editor.putString("transactions", updatedTransactions)
        editor.apply()

        Toast.makeText(this, "Goal created: $goalName", Toast.LENGTH_SHORT).show()
        finish()
    }
}