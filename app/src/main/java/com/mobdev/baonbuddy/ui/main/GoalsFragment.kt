package com.mobdev.baonbuddy.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.models.Goal
import com.mobdev.baonbuddy.ui.adapters.GoalAdapter
import com.mobdev.baonbuddy.ui.goals.SetGoalActivity

class GoalsFragment : Fragment() {

    private lateinit var goalsRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var addGoalButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalsRecyclerView = view.findViewById(R.id.goalsRecyclerView)
        emptyState = view.findViewById(R.id.emptyState)
        addGoalButton = view.findViewById(R.id.addGoalButton)

        // Add Goal button
        addGoalButton.setOnClickListener {
            val intent = Intent(requireContext(), SetGoalActivity::class.java)
            startActivity(intent)
        }

        // Load goals
        loadGoals()
    }

    override fun onResume() {
        super.onResume()
        loadGoals()
    }

    private fun loadGoals() {
        val sharedPref = requireActivity().getSharedPreferences("BaonBuddyPrefs", Context.MODE_PRIVATE)
        val goalsJson = sharedPref.getString("goals", "[]") ?: "[]"

        val goals = mutableListOf<Goal>()

        if (goalsJson != "[]") {
            try {
                val items = goalsJson
                    .removeSurrounding("[", "]")
                    .split("},")
                    .map { it.trim().removeSuffix("}").removePrefix("{") + "}" }

                for (item in items) {
                    val nameMatch = Regex(""""name":"([^"]+)"""").find(item)
                    val targetAmountMatch = Regex(""""targetAmount":(\d+\.?\d*)""").find(item)
                    val savedAmountMatch = Regex(""""savedAmount":(\d+\.?\d*)""").find(item)
                    val targetDateMatch = Regex(""""targetDate":"([^"]*)"""").find(item)
                    val createdDateMatch = Regex(""""createdDate":"([^"]*)"""").find(item)

                    if (nameMatch != null && targetAmountMatch != null) {
                        goals.add(
                            Goal(
                                name = nameMatch.groupValues[1],
                                targetAmount = targetAmountMatch.groupValues[1].toDouble(),
                                savedAmount = savedAmountMatch?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0,
                                targetDate = targetDateMatch?.groupValues?.get(1) ?: "",
                                createdDate = createdDateMatch?.groupValues?.get(1) ?: ""
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Show empty state or list
        if (goals.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            goalsRecyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            goalsRecyclerView.visibility = View.VISIBLE

            goalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            goalsRecyclerView.adapter = GoalAdapter(goals) { goal ->
                val intent = Intent(requireContext(), com.mobdev.baonbuddy.ui.goals.AddToGoalActivity::class.java)
                intent.putExtra("goal_name", goal.name)
                intent.putExtra("goal_target", goal.targetAmount)
                intent.putExtra("goal_saved", goal.savedAmount)
                startActivity(intent)
            }
        }
    }
}