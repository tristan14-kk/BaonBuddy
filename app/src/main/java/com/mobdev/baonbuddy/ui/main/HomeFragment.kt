package com.mobdev.baonbuddy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.adapters.TransactionAdapter
import com.mobdev.baonbuddy.ui.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val userAvatar = view.findViewById<ImageView>(R.id.userAvatar)
        val userName = view.findViewById<TextView>(R.id.userName)
        val balanceAmount = view.findViewById<TextView>(R.id.balanceAmount)
        val settingsButton = view.findViewById<ImageView>(R.id.settingsButton)
        val addMoneyButton = view.findViewById<LinearLayout>(R.id.addMoneyButton)
        val spentMoneyButton = view.findViewById<LinearLayout>(R.id.spentMoneyButton)
        val setGoalsButton = view.findViewById<LinearLayout>(R.id.setGoalsButton)
        val recentActivityList = view.findViewById<RecyclerView>(R.id.recentActivityList)

        transactionAdapter = TransactionAdapter(emptyList())
        recentActivityList.layoutManager = LinearLayoutManager(requireContext())
        recentActivityList.adapter = transactionAdapter

        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = transactionAdapter.getTransactionAt(position)
                viewModel.deleteTransaction(transaction)
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recentActivityList)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                userName.text = "${it.name}!"
                userAvatar.setImageResource(it.avatarResId)
                val formatter = NumberFormat.getNumberInstance(Locale.US)
                formatter.minimumFractionDigits = 2
                formatter.maximumFractionDigits = 2
                balanceAmount.text = "₱${formatter.format(it.balance)}"
            }
        }

        viewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.updateTransactions(transactions)
        }

        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        addMoneyButton.setOnClickListener {
            findNavController().navigate(R.id.addMoneyFragment)
        }

        spentMoneyButton.setOnClickListener {
            findNavController().navigate(R.id.spentMoneyFragment)
        }

        setGoalsButton.setOnClickListener {
            findNavController().navigate(R.id.setGoalFragment)
        }
    }
}