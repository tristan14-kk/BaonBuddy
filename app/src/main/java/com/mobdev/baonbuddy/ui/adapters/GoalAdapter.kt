package com.mobdev.baonbuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.database.entity.GoalEntity
import java.text.NumberFormat
import java.util.*

class GoalAdapter(
    private var goals: List<GoalEntity>,
    private val onGoalClick: (GoalEntity) -> Unit
) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalName: TextView = itemView.findViewById(R.id.goalName)
        val goalDate: TextView = itemView.findViewById(R.id.goalDate)
        val goalTarget: TextView = itemView.findViewById(R.id.goalTarget)
        val goalProgress: ProgressBar = itemView.findViewById(R.id.goalProgress)
        val goalSaved: TextView = itemView.findViewById(R.id.goalSaved)
        val goalPercentage: TextView = itemView.findViewById(R.id.goalPercentage)
        val addToGoalButton: Button = itemView.findViewById(R.id.addToGoalButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]

        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0

        holder.goalName.text = goal.name
        holder.goalDate.text = if (goal.targetDate.isNotEmpty()) "Target: ${goal.targetDate}" else ""
        holder.goalTarget.text = "₱${formatter.format(goal.targetAmount)}"
        holder.goalSaved.text = "₱${formatter.format(goal.savedAmount)} saved"

        val progress = if (goal.targetAmount > 0) {
            ((goal.savedAmount / goal.targetAmount) * 100).toInt()
        } else 0
        holder.goalProgress.progress = progress.coerceIn(0, 100)
        holder.goalPercentage.text = "${progress.coerceIn(0, 100)}%"

        holder.addToGoalButton.setOnClickListener { onGoalClick(goal) }
        holder.itemView.setOnClickListener { onGoalClick(goal) }
    }

    override fun getItemCount(): Int = goals.size

    fun updateGoals(newGoals: List<GoalEntity>) {
        goals = newGoals
        notifyDataSetChanged()
    }

    fun getGoalAt(position: Int): GoalEntity {
        return goals[position]
    }
}