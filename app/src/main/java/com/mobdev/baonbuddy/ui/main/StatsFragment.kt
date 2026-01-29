package com.mobdev.baonbuddy.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobdev.baonbuddy.R

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadStats()
    }

    override fun onResume() {
        super.onResume()
        loadStats()
    }

    private fun loadStats() {
        val view = view ?: return

        val moneyInAmount = view.findViewById<TextView>(R.id.moneyInAmount)
        val moneyOutAmount = view.findViewById<TextView>(R.id.moneyOutAmount)

        val categoryFoodAmount = view.findViewById<TextView>(R.id.categoryFoodAmount)
        val categoryFoodProgress = view.findViewById<ProgressBar>(R.id.categoryFoodProgress)
        val categoryFoodLayout = view.findViewById<LinearLayout>(R.id.categoryFoodLayout)

        val categoryTransportAmount = view.findViewById<TextView>(R.id.categoryTransportAmount)
        val categoryTransportProgress = view.findViewById<ProgressBar>(R.id.categoryTransportProgress)
        val categoryTransportLayout = view.findViewById<LinearLayout>(R.id.categoryTransportLayout)

        val categorySchoolAmount = view.findViewById<TextView>(R.id.categorySchoolAmount)
        val categorySchoolProgress = view.findViewById<ProgressBar>(R.id.categorySchoolProgress)
        val categorySchoolLayout = view.findViewById<LinearLayout>(R.id.categorySchoolLayout)

        val categoryOtherAmount = view.findViewById<TextView>(R.id.categoryOtherAmount)
        val categoryOtherProgress = view.findViewById<ProgressBar>(R.id.categoryOtherProgress)
        val categoryOtherLayout = view.findViewById<LinearLayout>(R.id.categoryOtherLayout)

        val streakDays = view.findViewById<TextView>(R.id.streakDays)

        // Load transactions
        val sharedPref = requireActivity().getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)
        val transactionsJson = sharedPref.getString("transactions", "[]") ?: "[]"

        var totalMoneyIn = 0.0
        var totalMoneyOut = 0.0
        var foodTotal = 0.0
        var transportTotal = 0.0
        var schoolTotal = 0.0
        var otherTotal = 0.0

        // Parse transactions
        if (transactionsJson != "[]") {
            try {
                val items = transactionsJson
                    .removeSurrounding("[", "]")
                    .split("},")
                    .map { it.trim().removeSuffix("}").removePrefix("{") + "}" }

                for (item in items) {
                    val titleMatch = Regex(""""title":"([^"]+)"""").find(item)
                    val amountMatch = Regex(""""amount":(\d+\.?\d*)""").find(item)
                    val isIncomeMatch = Regex(""""isIncome":(true|false)""").find(item)

                    if (amountMatch != null && isIncomeMatch != null) {
                        val amount = amountMatch.groupValues[1].toDouble()
                        val isIncome = isIncomeMatch.groupValues[1].toBoolean()
                        val title = titleMatch?.groupValues?.get(1) ?: ""

                        if (isIncome) {
                            totalMoneyIn += amount
                        } else {
                            totalMoneyOut += amount

                            // Categorize spending
                            when {
                                title.contains("Food", ignoreCase = true) ||
                                        title.contains("Lunch", ignoreCase = true) ||
                                        title.contains("Breakfast", ignoreCase = true) ||
                                        title.contains("Dinner", ignoreCase = true) ||
                                        title.contains("Snack", ignoreCase = true) -> {
                                    foodTotal += amount
                                }
                                title.contains("Transport", ignoreCase = true) ||
                                        title.contains("Fare", ignoreCase = true) ||
                                        title.contains("Jeep", ignoreCase = true) ||
                                        title.contains("Bus", ignoreCase = true) ||
                                        title.contains("Grab", ignoreCase = true) -> {
                                    transportTotal += amount
                                }
                                title.contains("School", ignoreCase = true) ||
                                        title.contains("Book", ignoreCase = true) ||
                                        title.contains("Project", ignoreCase = true) ||
                                        title.contains("Supply", ignoreCase = true) -> {
                                    schoolTotal += amount
                                }
                                else -> {
                                    otherTotal += amount
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Update UI
        moneyInAmount.text = "₱${String.format("%.2f", totalMoneyIn)}"
        moneyOutAmount.text = "₱${String.format("%.2f", totalMoneyOut)}"

        // Calculate max for progress bars
        val maxSpending = maxOf(foodTotal, transportTotal, schoolTotal, otherTotal, 1.0)

        // Food
        categoryFoodAmount.text = "₱${String.format("%.2f", foodTotal)}"
        categoryFoodProgress.progress = ((foodTotal / maxSpending) * 100).toInt()
        categoryFoodLayout.visibility = if (foodTotal > 0) View.VISIBLE else View.VISIBLE

        // Transport
        categoryTransportAmount.text = "₱${String.format("%.2f", transportTotal)}"
        categoryTransportProgress.progress = ((transportTotal / maxSpending) * 100).toInt()
        categoryTransportLayout.visibility = if (transportTotal > 0) View.VISIBLE else View.VISIBLE

        // School
        categorySchoolAmount.text = "₱${String.format("%.2f", schoolTotal)}"
        categorySchoolProgress.progress = ((schoolTotal / maxSpending) * 100).toInt()
        categorySchoolLayout.visibility = if (schoolTotal > 0) View.VISIBLE else View.VISIBLE

        // Other
        categoryOtherAmount.text = "₱${String.format("%.2f", otherTotal)}"
        categoryOtherProgress.progress = ((otherTotal / maxSpending) * 100).toInt()
        categoryOtherLayout.visibility = if (otherTotal > 0) View.VISIBLE else View.VISIBLE

        // Calculate savings streak (days since app was used)
        val lastSaveDate = sharedPref.getLong("last_save_date", 0)
        val streakCount = sharedPref.getInt("savings_streak", 0)

        val currentTime = System.currentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000

        if (lastSaveDate == 0L) {
            // First time - start streak
            sharedPref.edit().putLong("last_save_date", currentTime).apply()
            sharedPref.edit().putInt("savings_streak", 1).apply()
            streakDays.text = "1 Day"
        } else {
            val daysDiff = ((currentTime - lastSaveDate) / oneDayMs).toInt()

            if (daysDiff <= 1) {
                streakDays.text = "$streakCount Days"
            } else {
                // Streak broken, reset
                sharedPref.edit().putInt("savings_streak", 1).apply()
                sharedPref.edit().putLong("last_save_date", currentTime).apply()
                streakDays.text = "1 Day"
            }
        }
    }
}