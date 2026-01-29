package com.mobdev.baonbuddy.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdev.baonbuddy.R

class SetPinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_pin)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val pin1 = findViewById<EditText>(R.id.pin1)
        val pin2 = findViewById<EditText>(R.id.pin2)
        val pin3 = findViewById<EditText>(R.id.pin3)
        val pin4 = findViewById<EditText>(R.id.pin4)
        val skipButton = findViewById<TextView>(R.id.skipButton)
        val setPinButton = findViewById<Button>(R.id.setPinButton)

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Auto-move to next PIN box
        setupPinInput(pin1, pin2)
        setupPinInput(pin2, pin3)
        setupPinInput(pin3, pin4)
        setupPinInput(pin4, null)

        // Skip button
        skipButton.setOnClickListener {
            finish()
        }

        // Set PIN button
        setPinButton.setOnClickListener {
            val pinCode = "${pin1.text}${pin2.text}${pin3.text}${pin4.text}"

            if (pinCode.length != 4) {
                Toast.makeText(this, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save PIN
            val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
            sharedPref.edit().putString("user_pin", pinCode).apply()
            sharedPref.edit().putBoolean("pin_enabled", true).apply()

            Toast.makeText(this, "PIN set successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupPinInput(current: EditText, next: EditText?) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    next?.requestFocus()
                }
            }
        })
    }
}