package com.mobdev.baonbuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.models.Transaction

class TransactionAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.transactionTitle)
        val date: TextView = view.findViewById(R.id.transactionDate)
        val amount: TextView = view.findViewById(R.id.transactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.title.text = transaction.title
        holder.date.text = transaction.date

        if (transaction.isIncome) {
            holder.amount.text = "+ ₱${String.format("%.0f", transaction.amount)}"
            holder.amount.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.positive_green)
            )
        } else {
            holder.amount.text = "- ₱${String.format("%.0f", transaction.amount)}"
            holder.amount.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.negative_red)
            )
        }
    }

    override fun getItemCount() = transactions.size
}