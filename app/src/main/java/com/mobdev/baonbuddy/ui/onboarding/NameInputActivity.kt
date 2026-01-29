package com.mobdev.baonbuddy.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobdev.baonbuddy.R

class NameInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_input)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val continueButton = findViewById<Button>(R.id.continueButton)

        // Back button - go to previous screen
        backButton.setOnClickListener {
            finish()
        }

        // Continue button
        continueButton.setOnClickListener {
            val name = nameInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the name using SharedPreferences
            val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
            sharedPref.edit().putString("user_name", name).apply()

            // Go to Avatar Selection
            val intent = Intent(this, AvatarSelectionActivity::class.java)
            startActivity(intent)
        }
    }
}