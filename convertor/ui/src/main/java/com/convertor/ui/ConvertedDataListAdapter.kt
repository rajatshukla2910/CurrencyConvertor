package com.convertor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.convertor.data.Currency

import com.convertor.ui.databinding.CurrencyListItemBinding

class ConvertedDataListAdapter(val items: List<Currency>) : RecyclerView.Adapter<ConvertedDataListAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: CurrencyListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<CurrencyListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.currency_list_item,
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.executePendingBindings()
        holder.binding.apply {
            name.text = item.currency
            value.text = item.value.toString()
        }

    }

    override fun getItemCount(): Int = items.size

}