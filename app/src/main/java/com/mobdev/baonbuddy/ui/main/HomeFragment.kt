package com.mobdev.baonbuddy.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.models.Transaction
import com.mobdev.baonbuddy.ui.adapters.TransactionAdapter

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsButton = view.findViewById<ImageView>(R.id.settingsButton)
        val addMoneyButton = view.findViewById<LinearLayout>(R.id.addMoneyButton)
        val spentMoneyButton = view.findViewById<LinearLayout>(R.id.spentMoneyButton)
        val setGoalsButton = view.findViewById<LinearLayout>(R.id.setGoalsButton)

        // Settings button click
        settingsButton.setOnClickListener {
            val intent = Intent(requireContext(), com.mobdev.baonbuddy.ui.settings.SettingsActivity::class.java)
            startActivity(intent)
        }

        // Add Money button click
        addMoneyButton.setOnClickListener {
            val intent = Intent(
                requireContext(), com.mobdev.baonbuddy.ui.transactions.AddMoneyActivity::class.java)
            startActivity(intent)
        }

        // Spent Money button click
        spentMoneyButton.setOnClickListener {
            val intent = Intent(requireContext(), com.mobdev.baonbuddy.ui.transactions.SpentMoneyActivity::class.java)
            startActivity(intent)
        }

        // Set Goals button click
        setGoalsButton.setOnClickListener {
            val intent = Intent(requireContext(), com.mobdev.baonbuddy.ui.goals.SetGoalActivity::class.java)
            startActivity(intent)
        }

        // Load user data
        loadUserData()
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val view = view ?: return

        val userAvatar = view.findViewById<ImageView>(R.id.userAvatar)
        val userName = view.findViewById<TextView>(R.id.userName)
        val balanceAmount = view.findViewById<TextView>(R.id.balanceAmount)
        val recentActivityList = view.findViewById<RecyclerView>(R.id.recentActivityList)

        // Load user data from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)
        val name = sharedPref.getString("user_name", "Buddy") ?: "Buddy"
        val avatarId = sharedPref.getInt("user_avatar", R.drawable.avatar_snail)
        val balance = sharedPref.getFloat("user_balance", 0f)

        // Set user data
        userName.text = "$name!"
        userAvatar.setImageResource(avatarId)
        balanceAmount.text = "₱${String.format("%.2f", balance)}"

        // Load transactions
        loadTransactions(recentActivityList, sharedPref)
    }

    private fun loadTransactions(recentActivityList: RecyclerView, sharedPref: android.content.SharedPreferences) {
        val transactionsJson = sharedPref.getString("transactions", "[]") ?: "[]"

        val transactions = mutableListOf<Transaction>()

        // Parse JSON manually (simple approach)
        if (transactionsJson != "[]") {
            try {
                val items = transactionsJson
                    .removeSurrounding("[", "]")
                    .split("},")
                    .map { it.trim().removeSuffix("}").removePrefix("{") + "}" }

                for (item in items) {
                    val titleMatch = Regex(""""title":"([^"]+)"""").find(item)
                    val dateMatch = Regex(""""date":"([^"]+)"""").find(item)
                    val amountMatch = Regex(""""amount":(\d+\.?\d*)""").find(item)
                    val isIncomeMatch = Regex(""""isIncome":(true|false)""").find(item)

                    if (titleMatch != null && dateMatch != null && amountMatch != null && isIncomeMatch != null) {
                        transactions.add(
                            Transaction(
                                title = titleMatch.groupValues[1],
                                date = dateMatch.groupValues[1],
                                amount = amountMatch.groupValues[1].toDouble(),
                                isIncome = isIncomeMatch.groupValues[1].toBoolean()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Setup RecyclerView
        recentActivityList.layoutManager = LinearLayoutManager(requireContext())
        recentActivityList.adapter = TransactionAdapter(transactions)
    }
}