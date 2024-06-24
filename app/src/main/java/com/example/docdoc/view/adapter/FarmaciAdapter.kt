package com.example.docdoc.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R

class FarmaciAdapter(private var farmaci: MutableList<String>, private val onDelete: (Int) -> Unit) : RecyclerView.Adapter<FarmaciAdapter.FarmaciViewHolder>() {

    class FarmaciViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val farmacoTextView: TextView = itemView.findViewById(R.id.item)
        val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmaciViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_malattie_farmaci, parent, false)
        return FarmaciViewHolder(view)
    }

    override fun onBindViewHolder(holder: FarmaciViewHolder, position: Int) {
        holder.farmacoTextView.text = farmaci[position]
        holder.deleteButton.setOnClickListener {
            onDelete(position)
        }
    }

    override fun getItemCount() = farmaci.size

    fun updateData(newFarmaci: List<String>) {
        this.farmaci = newFarmaci.toMutableList()
        notifyDataSetChanged()
    }
}