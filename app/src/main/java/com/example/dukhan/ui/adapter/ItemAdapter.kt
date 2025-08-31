package com.example.dukhan.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dukhan.databinding.ItemDukhanBinding
import com.example.dukhan.domain.model.InventoryEntity

/**
 * Adapter class for displaying a list of [InventoryEntity] items in a RecyclerView.
 *
 * @property list The list of [InventoryEntity] items to be displayed.
 */
class ItemAdapter(private val list : List<InventoryEntity>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ItemDukhanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.binding.itemName.text = item.name
        holder.binding.itemCategory.text = item.category
        holder.binding.itemQTY.text = item.qty.toString()
    }

    override fun getItemCount(): Int = list.size

    inner class ItemViewHolder(val binding: ItemDukhanBinding) : RecyclerView.ViewHolder(binding.root)
}