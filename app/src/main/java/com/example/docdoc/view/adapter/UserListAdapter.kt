package com.example.docdoc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R
import com.example.docdoc.model.Utente

class UserListAdapter(private val dataList: List<Utente>): RecyclerView.Adapter<UserListAdapter.ViewHolderClass>() {

    var onItemClick: ((Utente) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        val nomeCognome = currentItem.nome + " " + currentItem.cognome
        holder.rvNomeCognome.text = nomeCognome
        holder.rvIndirizzo.text = currentItem.indirizzo

        holder.itemView.setOnClickListener{
            // al click dell'elemento viene restituito lo stesso al metodo onItemClick
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvNomeCognome:TextView = itemView.findViewById(R.id.show_nome_cognome)
        val rvIndirizzo:TextView = itemView.findViewById(R.id.show_indirizzo)
    }
}