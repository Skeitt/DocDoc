package com.example.docdoc.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R

class MalattieAdapter(private var malattie: MutableList<String>, private val onDelete: (Int) -> Unit) : RecyclerView.Adapter<MalattieAdapter.MalattieViewHolder>() {

    class MalattieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val malattiaTextView: TextView = itemView.findViewById(R.id.item)
        val deleteButton: ImageButton = itemView.findViewById(R.id.button_delete_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MalattieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_malattie_farmaci, parent, false)
        return MalattieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MalattieViewHolder, position: Int) {
        holder.malattiaTextView.text = malattie[position]
        holder.deleteButton.setOnClickListener {
            onDelete(position)
        }
    }

    override fun getItemCount() = malattie.size

    fun updateData(newMalattie: List<String>) {
        this.malattie = newMalattie.toMutableList()
        notifyDataSetChanged()
    }
}