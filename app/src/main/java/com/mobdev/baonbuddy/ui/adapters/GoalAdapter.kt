package com.mobdev.baonbuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.models.Goal

class GoalAdapter(
    private val goals: List<Goal>,
    private val onAddMoneyClick: (Goal) -> Unit
) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalName: TextView = view.findViewById(R.id.goalName)
        val goalDate: TextView = view.findViewById(R.id.goalDate)
        val goalTarget: TextView = view.findViewById(R.id.goalTarget)
        val goalProgress: ProgressBar = view.findViewById(R.id.goalProgress)
        val goalSaved: TextView = view.findViewById(R.id.goalSaved)
        val goalPercentage: TextView = view.findViewById(R.id.goalPercentage)
        val addToGoalButton: Button = view.findViewById(R.id.addToGoalButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        holder.goalName.text = goal.name
        holder.goalTarget.text = "₱${String.format("%,.0f", goal.targetAmount)}"

        // Show target date
        if (goal.targetDate.isNotEmpty()) {
            holder.goalDate.text = "Target: ${goal.targetDate}"
            holder.goalDate.visibility = View.VISIBLE
        } else {
            holder.goalDate.visibility = View.GONE
        }

        // Calculate progress
        val progress = if (goal.targetAmount > 0) {
            ((goal.savedAmount / goal.targetAmount) * 100).toInt()
        } else {
            0
        }

        holder.goalProgress.progress = progress.coerceAtMost(100)
        holder.goalSaved.text = "₱${String.format("%,.0f", goal.savedAmount)} saved"
        holder.goalPercentage.text = "${progress.coerceAtMost(100)}%"

        if (progress >= 100) {
            holder.addToGoalButton.text = "✓ Goal Complete!"
            holder.addToGoalButton.isEnabled = false
            holder.addToGoalButton.alpha = 0.5f
        } else {
            holder.addToGoalButton.text = "+ Add Money to Goal"
            holder.addToGoalButton.isEnabled = true
            holder.addToGoalButton.alpha = 1f
        }

        // Add money button click
        holder.addToGoalButton.setOnClickListener {
            if (progress < 100) {
                onAddMoneyClick(goal)
            }
        }
    }

    override fun getItemCount() = goals.size
}