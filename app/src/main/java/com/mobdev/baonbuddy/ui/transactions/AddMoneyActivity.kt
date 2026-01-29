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

class AddMoneyActivity : AppCompatActivity() {

    private var selectedSource: String = ""
    private val sourceButtons = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_money)

        // Get views
        val backButton = findViewById<ImageView>(R.id.backButton)
        val amountInput = findViewById<EditText>(R.id.amountInput)
        val noteInput = findViewById<EditText>(R.id.noteInput)
        val addButton = findViewById<Button>(R.id.addButton)

        // Quick add buttons
        val quickAdd50 = findViewById<TextView>(R.id.quickAdd50)
        val quickAdd100 = findViewById<TextView>(R.id.quickAdd100)
        val quickAdd200 = findViewById<TextView>(R.id.quickAdd200)

        // Source buttons
        val sourceParents = findViewById<TextView>(R.id.sourceParents)
        val sourceGift = findViewById<TextView>(R.id.sourceGift)
        val sourceChores = findViewById<TextView>(R.id.sourceChores)
        val sourceOther = findViewById<TextView>(R.id.sourceOther)

        sourceButtons.addAll(listOf(sourceParents, sourceGift, sourceChores, sourceOther))

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Quick add click listeners
        quickAdd50.setOnClickListener {
            amountInput.setText("50.00")
        }
        quickAdd100.setOnClickListener {
            amountInput.setText("100.00")
        }
        quickAdd200.setOnClickListener {
            amountInput.setText("200.00")
        }

        // Source button click listeners
        sourceParents.setOnClickListener { selectSource(sourceParents, "Parents") }
        sourceGift.setOnClickListener { selectSource(sourceGift, "Gift") }
        sourceChores.setOnClickListener { selectSource(sourceChores, "Chores") }
        sourceOther.setOnClickListener { selectSource(sourceOther, "Other") }

        // Add button
        addButton.setOnClickListener {
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

            // Save the transaction
            saveTransaction(amount, noteInput.text.toString().trim())
        }
    }

    private fun selectSource(selectedButton: TextView, source: String) {
        // Reset all buttons
        sourceButtons.forEach { button ->
            button.background = ContextCompat.getDrawable(this, R.drawable.button_outline)
            button.setTextColor(ContextCompat.getColor(this, R.color.text_dark))
        }

        // Highlight selected button
        selectedButton.background = ContextCompat.getDrawable(this, R.drawable.button_selected)
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white))

        selectedSource = source
    }

    private fun saveTransaction(amount: Double, note: String) {
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // Update balance
        val currentBalance = sharedPref.getFloat("user_balance", 0f)
        val newBalance = currentBalance + amount.toFloat()
        editor.putFloat("user_balance", newBalance)

        // Save transaction to list
        val transactionsJson = sharedPref.getString("transactions", "[]") ?: "[]"
        val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Create transaction title
        val title = if (selectedSource.isNotEmpty()) {
            if (note.isNotEmpty()) "$selectedSource - $note" else "Added from $selectedSource"
        } else {
            if (note.isNotEmpty()) note else "Added Baon"
        }

        // Simple JSON append
        val newTransaction = """{"title":"$title","date":"$currentDate","amount":$amount,"isIncome":true}"""
        val updatedJson = if (transactionsJson == "[]") {
            "[$newTransaction]"
        } else {
            "[$newTransaction,${transactionsJson.substring(1)}"
        }

        editor.putString("transactions", updatedJson)
        editor.apply()

        Toast.makeText(this, "₱${String.format("%.2f", amount)} added!", Toast.LENGTH_SHORT).show()
        finish()
    }
}