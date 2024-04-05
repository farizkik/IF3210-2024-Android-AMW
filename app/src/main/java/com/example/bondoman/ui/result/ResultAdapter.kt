package com.example.bondoman.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.core.data.Item
import com.example.bondoman.databinding.ItemScanBinding
import com.example.bondoman.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.util.Locale

class ResultAdapter(var items: ArrayList<Item>): RecyclerView.Adapter<ResultAdapter.ResultHolder>() {

    inner class ResultHolder(val binding: ItemScanBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Item) {
            binding.NameText.text = get.name
            binding.AmountText.text = get.qty.toString()

            val price = get.price.toString().toDouble()
            val normalized = (price * 1000).toLong()
            val formatter = NumberFormat.getInstance(Locale.getDefault())
            val formattedNumber = "Rp " + formatter.format(normalized)
            binding.NominalText.text = formattedNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val binding = ItemScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newItems: ArrayList<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}