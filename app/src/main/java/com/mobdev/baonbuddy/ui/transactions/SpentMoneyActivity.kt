package com.mobdev.baonbuddy.ui.transactions

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobdev.baonbuddy.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SpentMoneyActivity : AppCompatActivity() {

    private var selectedCategory: String = ""
    private val categoryButtons = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spent_money)

        // Get views
        val backButton = findViewById<ImageView>(R.id.backButton)
        val amountInput = findViewById<EditText>(R.id.amountInput)
        val noteInput = findViewById<EditText>(R.id.noteInput)
        val recordButton = findViewById<Button>(R.id.recordButton)

        // Quick amount buttons
        val quickAmount50 = findViewById<TextView>(R.id.quickAmount50)
        val quickAmount100 = findViewById<TextView>(R.id.quickAmount100)
        val quickAmount200 = findViewById<TextView>(R.id.quickAmount200)

        // Category buttons
        val categoryFood = findViewById<TextView>(R.id.categoryFood)
        val categoryTransport = findViewById<TextView>(R.id.categoryTransport)
        val categorySchool = findViewById<TextView>(R.id.categorySchool)
        val categoryOther = findViewById<TextView>(R.id.categoryOther)

        categoryButtons.addAll(listOf(categoryFood, categoryTransport, categorySchool, categoryOther))

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Quick amount click listeners
        quickAmount50.setOnClickListener {
            amountInput.setText("50.00")
        }
        quickAmount100.setOnClickListener {
            amountInput.setText("100.00")
        }
        quickAmount200.setOnClickListener {
            amountInput.setText("200.00")
        }

        // Category button click listeners
        categoryFood.setOnClickListener { selectCategory(categoryFood, "Food") }
        categoryTransport.setOnClickListener { selectCategory(categoryTransport, "Transport") }
        categorySchool.setOnClickListener { selectCategory(categorySchool, "School") }
        categoryOther.setOnClickListener { selectCategory(categoryOther, "Other") }

        // Record button
        recordButton.setOnClickListener {
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

            // Save the expense
            saveExpense(amount, noteInput.text.toString().trim())
        }
    }

    private fun selectCategory(selectedButton: TextView, category: String) {
        // Reset all buttons
        categoryButtons.forEach { button ->
            button.background = ContextCompat.getDrawable(this, R.drawable.button_outline)
            button.setTextColor(ContextCompat.getColor(this, R.color.text_dark))
        }

        // Highlight selected button
        selectedButton.background = ContextCompat.getDrawable(this, R.drawable.button_selected)
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white))

        selectedCategory = category
    }

    private fun saveExpense(amount: Double, note: String) {
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Update balance (subtract expense)
        val currentBalance = sharedPref.getFloat("user_balance", 0f)
        val newBalance = currentBalance - amount.toFloat()
        editor.putFloat("user_balance", newBalance)

        // Save transaction to list
        val transactionsJson = sharedPref.getString("transactions", "[]") ?: "[]"
        val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Create transaction title
        val title = if (selectedCategory.isNotEmpty()) {
            if (note.isNotEmpty()) "$selectedCategory - $note" else selectedCategory
        } else {
            if (note.isNotEmpty()) note else "Expense"
        }

        // Simple JSON append
        val newTransaction = """{"title":"$title","date":"$currentDate","amount":$amount,"isIncome":false}"""
        val updatedJson = if (transactionsJson == "[]") {
            "[$newTransaction]"
        } else {
            "[$newTransaction,${transactionsJson.substring(1)}"
        }

        editor.putString("transactions", updatedJson)
        editor.apply()

        Toast.makeText(this, "₱${String.format("%.2f", amount)} recorded!", Toast.LENGTH_SHORT).show()
        finish()
    }
}