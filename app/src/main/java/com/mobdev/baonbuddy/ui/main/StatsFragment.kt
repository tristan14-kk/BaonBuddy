package com.mobdev.baonbuddy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.ui.viewmodel.StatsViewModel
import java.text.NumberFormat
import java.util.Locale

class StatsFragment : Fragment() {

    private lateinit var viewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[StatsViewModel::class.java]

        val moneyInAmount = view.findViewById<TextView>(R.id.moneyInAmount)
        val moneyOutAmount = view.findViewById<TextView>(R.id.moneyOutAmount)
        val categoryFoodProgress = view.findViewById<ProgressBar>(R.id.categoryFoodProgress)
        val categoryTransportProgress = view.findViewById<ProgressBar>(R.id.categoryTransportProgress)
        val categorySchoolProgress = view.findViewById<ProgressBar>(R.id.categorySchoolProgress)
        val categoryOtherProgress = view.findViewById<ProgressBar>(R.id.categoryOtherProgress)
        val categoryFoodAmount = view.findViewById<TextView>(R.id.categoryFoodAmount)
        val categoryTransportAmount = view.findViewById<TextView>(R.id.categoryTransportAmount)
        val categorySchoolAmount = view.findViewById<TextView>(R.id.categorySchoolAmount)
        val categoryOtherAmount = view.findViewById<TextView>(R.id.categoryOtherAmount)

        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2

        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            moneyInAmount.text = "₱${formatter.format(income ?: 0.0)}"
        }

        viewModel.totalExpenses.observe(viewLifecycleOwner) { expenses ->
            moneyOutAmount.text = "₱${formatter.format(expenses ?: 0.0)}"

            val total = if ((expenses ?: 0.0) > 0) expenses!! else 1.0

            viewModel.foodExpenses.observe(viewLifecycleOwner) { food ->
                val amount = food ?: 0.0
                categoryFoodAmount.text = "₱${formatter.format(amount)}"
                categoryFoodProgress.progress = ((amount / total) * 100).toInt().coerceIn(0, 100)
            }

            viewModel.transportExpenses.observe(viewLifecycleOwner) { transport ->
                val amount = transport ?: 0.0
                categoryTransportAmount.text = "₱${formatter.format(amount)}"
                categoryTransportProgress.progress = ((amount / total) * 100).toInt().coerceIn(0, 100)
            }

            viewModel.schoolExpenses.observe(viewLifecycleOwner) { school ->
                val amount = school ?: 0.0
                categorySchoolAmount.text = "₱${formatter.format(amount)}"
                categorySchoolProgress.progress = ((amount / total) * 100).toInt().coerceIn(0, 100)
            }

            viewModel.otherExpenses.observe(viewLifecycleOwner) { other ->
                val amount = other ?: 0.0
                categoryOtherAmount.text = "₱${formatter.format(amount)}"
                categoryOtherProgress.progress = ((amount / total) * 100).toInt().coerceIn(0, 100)
            }
        }
    }
}