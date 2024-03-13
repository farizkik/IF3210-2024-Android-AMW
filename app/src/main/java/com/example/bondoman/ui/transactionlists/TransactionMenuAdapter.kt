package com.example.bondoman.ui.transactionlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date

class TransactionMenuAdapter(var transactions : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionMenuAdapter.TransactionViewHolder>(){

    inner class TransactionViewHolder(val binding: ItemTransactionBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(get: Transaction){
            binding.title.text = get.title
            binding.content.text = get.type

            val sdf = SimpleDateFormat("MMM dd, HH:mm:ss")
            val resultDate = Date(get.creationTime)
            binding.date.text = "Created:       ${sdf.format(resultDate)}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    fun updateTransactions(newTransactions: List<Transaction>){
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }
}