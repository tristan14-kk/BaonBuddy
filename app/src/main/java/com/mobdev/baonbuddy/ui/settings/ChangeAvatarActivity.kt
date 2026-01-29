package com.mobdev.baonbuddy.ui.settings

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.adapters.AvatarAdapter

class ChangeAvatarActivity : AppCompatActivity() {

    private var selectedAvatarId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_avatar)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val avatarGrid = findViewById<RecyclerView>(R.id.avatarGrid)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Get current avatar
        val sharedPref = getSharedPreferences("BaonBuddyPrefs", MODE_PRIVATE)
        selectedAvatarId = sharedPref.getInt("user_avatar", R.drawable.avatar_snail)

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

        // Setup RecyclerView
        avatarGrid.layoutManager = GridLayoutManager(this, 3)
        avatarGrid.adapter = AvatarAdapter(avatars) { avatarId ->
            selectedAvatarId = avatarId
        }

        // Save button
        saveButton.setOnClickListener {
            if (selectedAvatarId == -1) {
                Toast.makeText(this, "Please select an avatar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPref.edit().putInt("user_avatar", selectedAvatarId).apply()
            Toast.makeText(this, "Avatar updated!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}