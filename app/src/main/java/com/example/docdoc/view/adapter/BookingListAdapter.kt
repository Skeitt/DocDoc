package com.example.docdoc.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R
import com.example.docdoc.model.Prenotazione

class BookingListAdapter(private val bookingList: List<Prenotazione>,private val isCompressed: Boolean = true): RecyclerView.Adapter<BookingListAdapter.ViewHolderClass>() {

    var onItemClick: ((Prenotazione) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(
            if (isCompressed) R.layout.item_list_booking else R.layout.item_list_complete_booking,
            parent,
            false
        )
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = bookingList[position]
        holder.rvOrario.text = currentItem.orario
        if(currentItem.nomePaziente != null && currentItem.cognomePaziente != null && isCompressed){
            val text = currentItem.nomePaziente + " " + currentItem.cognomePaziente
            holder.rvSlotDesc.text = text
        }
        else if(currentItem.descrizione != null){
            val text = currentItem.descrizione
            holder.rvSlotDesc.text = text
        }
        if(!isCompressed)
            holder.rvData.text = currentItem.data

        holder.itemView.setOnClickListener{
            // al click dell'elemento viene restituito lo stesso al metodo onItemClick
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvOrario:TextView = itemView.findViewById(R.id.show_orario)
        val rvSlotDesc:TextView = itemView.findViewById(R.id.show_slot_desc)
        val rvData: TextView = itemView.findViewById(R.id.show_data)
    }
}