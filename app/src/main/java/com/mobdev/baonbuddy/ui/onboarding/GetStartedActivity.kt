package com.mobdev.baonbuddy.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdev.baonbuddy.R

class GetStartedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        // Find the Get Started button and set click listener
        val getStartedButton = findViewById<android.widget.TextView>(R.id.getStartedButton)

        getStartedButton.setOnClickListener {
            val intent = Intent(this, NameInputActivity::class.java)
            startActivity(intent)
        }
    }
}