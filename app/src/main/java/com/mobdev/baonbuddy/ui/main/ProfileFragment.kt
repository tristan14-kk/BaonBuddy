package com.mobdev.baonbuddy.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.settings.SettingsActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsButton = view.findViewById<CardView>(R.id.settingsButton)
        val editAvatarButton = view.findViewById<TextView>(R.id.editAvatarButton)

        // Settings button
        settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        // Edit Avatar button
        editAvatarButton.setOnClickListener {
            val intent = Intent(requireContext(), com.mobdev.baonbuddy.ui.settings.ChangeAvatarActivity::class.java)
            startActivity(intent)
        }

        loadProfile()
    }

    override fun onResume() {
        super.onResume()
        loadProfile()
    }

    private fun loadProfile() {
        val view = view ?: return

        val userAvatar = view.findViewById<ImageView>(R.id.userAvatar)
        val userName = view.findViewById<TextView>(R.id.userName)
        val memberSince = view.findViewById<TextView>(R.id.memberSince)
        val totalSaved = view.findViewById<TextView>(R.id.totalSaved)
        val goalsCompleted = view.findViewById<TextView>(R.id.goalsCompleted)
        val currentStreak = view.findViewById<TextView>(R.id.currentStreak)

        val sharedPref = requireActivity().getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)

        // Load user info
        val name = sharedPref.getString("user_name", "Buddy") ?: "Buddy"
        val avatarId = sharedPref.getInt("user_avatar", R.drawable.avatar_placeholder)
        val balance = sharedPref.getFloat("user_balance", 0f)
        val streak = sharedPref.getInt("savings_streak", 1)

        // Set user info
        userName.text = name
        userAvatar.setImageResource(avatarId)

        // Member since (use first launch date or current date)
        var joinDate = sharedPref.getLong("join_date", 0)
        if (joinDate == 0L) {
            joinDate = System.currentTimeMillis()
            sharedPref.edit().putLong("join_date", joinDate).apply()
        }
        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        memberSince.text = "BaonBuddy since ${dateFormat.format(Date(joinDate))}"

        // Total saved (balance)
        totalSaved.text = "₱${String.format("%,.0f", balance.toDouble())}"

        // Goals completed
        val goalsJson = sharedPref.getString("goals", "[]") ?: "[]"
        var completedCount = 0

        if (goalsJson != "[]") {
            try {
                val items = goalsJson
                    .removeSurrounding("[", "]")
                    .split("},")
                    .map { it.trim().removeSuffix("}").removePrefix("{") + "}" }

                for (item in items) {
                    val targetAmountMatch = Regex(""""targetAmount":(\d+\.?\d*)""").find(item)
                    val savedAmountMatch = Regex(""""savedAmount":(\d+\.?\d*)""").find(item)

                    if (targetAmountMatch != null && savedAmountMatch != null) {
                        val target = targetAmountMatch.groupValues[1].toDouble()
                        val saved = savedAmountMatch.groupValues[1].toDouble()

                        if (saved >= target) {
                            completedCount++
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        goalsCompleted.text = "$completedCount"
        currentStreak.text = "$streak Days"
    }
}