package com.mobdev.baonbuddy.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import com.mobdev.baonbuddy.R

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply dark mode preference
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_landing)

        // Auto-navigate to GetStarted page after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}