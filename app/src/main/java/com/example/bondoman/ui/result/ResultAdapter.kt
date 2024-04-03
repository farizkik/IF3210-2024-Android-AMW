package com.example.bondoman.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.core.data.Item
import com.example.bondoman.databinding.ItemTransactionBinding

class ResultAdapter(var items: ArrayList<Item>): RecyclerView.Adapter<ResultAdapter.ResultHolder>() {

    // TODO: Replace with new Binding

    inner class ResultHolder(val binding: ItemTransactionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Item) {
            binding.title.text = get.name
            binding.content.text = get.qty.toString()
            binding.date.text = get.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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