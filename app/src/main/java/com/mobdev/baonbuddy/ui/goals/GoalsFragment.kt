package com.mobdev.baonbuddy.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.adapters.GoalAdapter
import com.mobdev.baonbuddy.ui.viewmodel.GoalsViewModel

class GoalsFragment : Fragment() {

    private lateinit var viewModel: GoalsViewModel
    private lateinit var goalAdapter: GoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GoalsViewModel::class.java]

        val goalsRecyclerView = view.findViewById<RecyclerView>(R.id.goalsRecyclerView)
        val emptyState = view.findViewById<LinearLayout>(R.id.emptyState)
        val addGoalButton = view.findViewById<Button>(R.id.addGoalButton)

        goalAdapter = GoalAdapter(emptyList()) { goal ->
            // Navigate to Add to Goal using Bundle
            val bundle = Bundle().apply {
                putInt("goalId", goal.id)
                putString("goalName", goal.name)
                putDouble("goalTarget", goal.targetAmount)
                putDouble("goalSaved", goal.savedAmount)
            }
            findNavController().navigate(R.id.addToGoalFragment, bundle)
        }
        goalsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        goalsRecyclerView.adapter = goalAdapter

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val goal = goalAdapter.getGoalAt(position)
                viewModel.deleteGoal(goal)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(goalsRecyclerView)

        viewModel.allGoals.observe(viewLifecycleOwner) { goals ->
            goalAdapter.updateGoals(goals)

            if (goals.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                goalsRecyclerView.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                goalsRecyclerView.visibility = View.VISIBLE
            }
        }

        addGoalButton.setOnClickListener {
            findNavController().navigate(R.id.setGoalFragment)
        }
    }
}