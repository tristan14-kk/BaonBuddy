package com.mobdev.baonbuddy.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.adapters.AvatarAdapter
import com.mobdev.baonbuddy.ui.main.MainActivity

class AvatarSelectionActivity : AppCompatActivity() {

    private var selectedAvatarId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_selection)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val avatarGrid = findViewById<RecyclerView>(R.id.avatarGrid)
        val letsGoButton = findViewById<Button>(R.id.letsGoButton)

        // Back button
        backButton.setOnClickListener {
            finish()
        }

        // Avatar list
        val avatars = listOf(
            R.drawable.avatar_snail,
            R.drawable.avatar_koala,
            R.drawable.avatar_octopus,
            R.drawable.avatar_chicken,
            R.drawable.avatar_elephant,
            R.drawable.avatar_penguin,
            R.drawable.avatar_pig,
            R.drawable.avatar_turtle,
            R.drawable.avatar_cat
        )

        // Setup RecyclerView with 3 columns
        avatarGrid.layoutManager = GridLayoutManager(this, 3)
        avatarGrid.adapter = AvatarAdapter(avatars) { avatarId ->
            selectedAvatarId = avatarId
        }

        // Let's Go button
        letsGoButton.setOnClickListener {
            if (selectedAvatarId == -1) {
                Toast.makeText(this, "Please select a buddy!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save selected avatar
            val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
            sharedPref.edit().putInt("user_avatar", selectedAvatarId).apply()

            // Mark onboarding as complete
            sharedPref.edit().putBoolean("onboarding_complete", true).apply()

            // Go to Main Activity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}