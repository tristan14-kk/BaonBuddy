package com.mobdev.baonbuddy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdev.baonbuddy.R
import com.mobdev.baonbuddy.data.database.entity.TransactionEntity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private var transactions: List<TransactionEntity>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.transactionTitle)
        val dateText: TextView = itemView.findViewById(R.id.transactionDate)
        val amountText: TextView = itemView.findViewById(R.id.transactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.titleText.text = transaction.title

        val dateFormat = SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault())
        holder.dateText.text = dateFormat.format(Date(transaction.timestamp))

        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0

        if (transaction.isIncome) {
            holder.amountText.text = "+ ₱${formatter.format(transaction.amount)}"
            holder.amountText.setTextColor(holder.itemView.context.getColor(R.color.positive_green))
        } else {
            holder.amountText.text = "- ₱${formatter.format(transaction.amount)}"
            holder.amountText.setTextColor(holder.itemView.context.getColor(R.color.negative_red))
        }
    }

    override fun getItemCount(): Int = transactions.size

    fun updateTransactions(newTransactions: List<TransactionEntity>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    fun getTransactionAt(position: Int): TransactionEntity {
        return transactions[position]
    }
}