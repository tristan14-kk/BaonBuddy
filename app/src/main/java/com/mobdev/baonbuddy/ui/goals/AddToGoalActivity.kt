package com.mobdev.baonbuddy.ui.goals

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdev.baonbuddy.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddToGoalActivity : AppCompatActivity() {

    private var goalName: String = ""
    private var goalTargetAmount: Double = 0.0
    private var goalSavedAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_goal)

        // Get goal data from intent
        goalName = intent.getStringExtra("goal_name") ?: ""
        goalTargetAmount = intent.getDoubleExtra("goal_target", 0.0)
        goalSavedAmount = intent.getDoubleExtra("goal_saved", 0.0)

        // Get views
        val backButton = findViewById<ImageView>(R.id.backButton)
        val goalNameText = findViewById<TextView>(R.id.goalNameText)
        val progressText = findViewById<TextView>(R.id.progressText)
        val amountInput = findViewById<EditText>(R.id.amountInput)
        val remainingAmount = findViewById<TextView>(R.id.remainingAmount)
        val addToGoalButton = findViewById<Button>(R.id.addToGoalButton)

        // Quick add buttons
        val quickAdd50 = findViewById<TextView>(R.id.quickAdd50)
        val quickAdd100 = findViewById<TextView>(R.id.quickAdd100)
        val quickAdd200 = findViewById<TextView>(R.id.quickAdd200)

        // Set goal info
        goalNameText.text = goalName

        val progress = if (goalTargetAmount > 0) {
            ((goalSavedAmount / goalTargetAmount) * 100).toInt()
        } else {
            0
        }
        progressText.text = "₱${String.format("%,.0f", goalSavedAmount)} / ₱${String.format("%,.0f", goalTargetAmount)} ($progress%)"

        val remaining = goalTargetAmount - goalSavedAmount
        remainingAmount.text = "₱${String.format("%,.0f", remaining.coerceAtLeast(0.0))}"

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Quick add click listeners
        quickAdd50.setOnClickListener {
            amountInput.setText("50")
        }
        quickAdd100.setOnClickListener {
            amountInput.setText("100")
        }
        quickAdd200.setOnClickListener {
            amountInput.setText("200")
        }

        // Add to goal button
        addToGoalButton.setOnClickListener {
            val amountText = amountInput.text.toString().trim()

            if (amountText.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if user has enough balance
            val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
            val currentBalance = sharedPref.getFloat("user_balance", 0f)

            if (amount > currentBalance) {
                Toast.makeText(this, "Not enough balance! You have ₱${String.format("%.2f", currentBalance)}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the amount to goal
            addMoneyToGoal(amount)
        }
    }

    private fun addMoneyToGoal(amount: Double) {
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Deduct from balance
        val currentBalance = sharedPref.getFloat("user_balance", 0f)
        val newBalance = currentBalance - amount.toFloat()
        editor.putFloat("user_balance", newBalance)

        // Update goal's saved amount
        val goalsJson = sharedPref.getString("goals", "[]") ?: "[]"
        var updatedGoalsJson = goalsJson

        if (goalsJson != "[]") {
            // Find and update the goal
            val newSavedAmount = goalSavedAmount + amount

            val goalPattern = """"name":"$goalName","targetAmount":$goalTargetAmount,"savedAmount":$goalSavedAmount"""
            val goalReplacement = """"name":"$goalName","targetAmount":$goalTargetAmount,"savedAmount":$newSavedAmount"""


            val patterns = listOf(
                """"name":"$goalName","targetAmount":${goalTargetAmount.toInt()}.0,"savedAmount":${goalSavedAmount.toInt()}.0""",
                """"name":"$goalName","targetAmount":${goalTargetAmount.toInt()},"savedAmount":${goalSavedAmount.toInt()}""",
                """"name":"$goalName","targetAmount":$goalTargetAmount,"savedAmount":${goalSavedAmount.toInt()}.0""",
                """"name":"$goalName","targetAmount":$goalTargetAmount,"savedAmount":${goalSavedAmount.toInt()}""",
                goalPattern
            )

            val replacements = listOf(
                """"name":"$goalName","targetAmount":${goalTargetAmount.toInt()}.0,"savedAmount":$newSavedAmount""",
                """"name":"$goalName","targetAmount":${goalTargetAmount.toInt()},"savedAmount":$newSavedAmount""",
                """"name":"$goalName","targetAmount":$goalTargetAmount,"savedAmount":$newSavedAmount""",
                """"name":"$goalName","targetAmount":$goalTargetAmount,"savedAmount":$newSavedAmount""",
                goalReplacement
            )

            for (i in patterns.indices) {
                if (goalsJson.contains(patterns[i])) {
                    updatedGoalsJson = goalsJson.replace(patterns[i], replacements[i])
                    break
                }
            }
        }

        editor.putString("goals", updatedGoalsJson)

        // Add transaction record
        val transactionsJson = sharedPref.getString("transactions", "[]") ?: "[]"
        val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val newTransaction = """{"title":"Added to: $goalName","date":"$currentDate","amount":$amount,"isIncome":false}"""
        val updatedTransactions = if (transactionsJson == "[]") {
            "[$newTransaction]"
        } else {
            "[$newTransaction,${transactionsJson.substring(1)}"
        }

        editor.putString("transactions", updatedTransactions)
        editor.apply()

        // Check if goal is complete
        val newTotal = goalSavedAmount + amount
        if (newTotal >= goalTargetAmount) {
            Toast.makeText(this, "🎉 Congratulations! You've reached your goal: $goalName!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "₱${String.format("%.0f", amount)} added to $goalName!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}