package com.example.docdoc.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R
import com.example.docdoc.model.Prenotazione

class BookingListAdapter(private val bookingList: List<Prenotazione>, private val isMedico: Boolean): RecyclerView.Adapter<BookingListAdapter.ViewHolderClass>() {

    var onItemClick: ((Prenotazione) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_booking, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = bookingList[position]
        holder.rvOrario.text = currentItem.orario
        if(currentItem.nomePaziente != null && currentItem.cognomePaziente != null){
            val text = currentItem.nomePaziente + " " + currentItem.cognomePaziente
            holder.rvSlotDesc.text = text
        }

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
    }
}