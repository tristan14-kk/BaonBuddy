package com.mobdev.baonbuddy.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.onboarding.LandingActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val editNameButton = findViewById<LinearLayout>(R.id.editNameButton)
        val changeAvatarButton = findViewById<LinearLayout>(R.id.changeAvatarButton)
        val setPinButton = findViewById<LinearLayout>(R.id.setPinButton)
        val darkModeSwitch = findViewById<SwitchCompat>(R.id.darkModeSwitch)
        val helpButton = findViewById<LinearLayout>(R.id.helpButton)
        val privacyButton = findViewById<LinearLayout>(R.id.privacyButton)
        val deleteDataButton = findViewById<LinearLayout>(R.id.deleteDataButton)

        // Load dark mode preference
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        darkModeSwitch.isChecked = isDarkMode

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Edit Name
        editNameButton.setOnClickListener {
            showEditNameDialog()
        }

        // Change Avatar
        changeAvatarButton.setOnClickListener {
            val intent = Intent(this, ChangeAvatarActivity::class.java)
            startActivity(intent)
        }

        // Set PIN
        setPinButton.setOnClickListener {
            val intent = Intent(this, SetPinActivity::class.java)
            startActivity(intent)
        }

        // Dark Mode Toggle
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            sharedPref.edit().putBoolean("dark_mode", isChecked).apply()

            // Apply dark mode
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Help & Support
        helpButton.setOnClickListener {
            val intent = Intent(this, HelpSupportActivity::class.java)
            startActivity(intent)
        }

        // Privacy Policy
        privacyButton.setOnClickListener {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        // Delete All Data
        deleteDataButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    private fun showEditNameDialog() {
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val currentName = sharedPref.getString("user_name", "") ?: ""

        val editText = android.widget.EditText(this)
        editText.setText(currentName)
        editText.setPadding(50, 30, 50, 30)

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setMessage("Enter your new name:")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newName = editText.text.toString().trim()
                if (newName.isNotEmpty()) {
                    sharedPref.edit().putString("user_name", newName).apply()
                    Toast.makeText(this, "Name updated to $newName!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete All Data")
            .setMessage("Are you sure you want to delete all your data? This action cannot be undone!")
            .setPositiveButton("Delete") { _, _ ->
                // Clear all data
                val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
                sharedPref.edit().clear().apply()

                Toast.makeText(this, "All data deleted!", Toast.LENGTH_SHORT).show()

                // Restart app from landing
                val intent = Intent(this, LandingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}