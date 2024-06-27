package com.example.docdoc.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R
import com.example.docdoc.model.Evento
import java.io.File

class EventListAdapter(private val eventList: List<Evento>): RecyclerView.Adapter<EventListAdapter.ViewHolderClass>() {
    var onItemClick: ((Evento) -> Unit)? = null
    var onFileItemClick: ((Evento, String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_evento, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = eventList[position]
        holder.rvMotivo.text = "Motivo: " + currentItem.motivo
        holder.rvSlotDesc.text = "Descrizione: " + currentItem.descrizione
        holder.rvSlotData.text = currentItem.data

        // RecyclerView di files annidata
        holder.rvFiles.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            val fileListAdapter = FileListAdapter(currentItem.listaFile ?: listOf())
            adapter = fileListAdapter

            fileListAdapter.onItemClick = {filename ->
                // quando viene cliccato il file deve essere invocata una funzione
                // che permette di recuperarlo sullo storage e di aprirlo
                onFileItemClick?.invoke(currentItem,filename)
            }
        }

        holder.rvButtonModifica.setOnClickListener{
            // al click del pulsante di modifica viene restituito l'evento stesso
            onItemClick?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvMotivo: TextView = itemView.findViewById(R.id.motivo_visita)
        val rvSlotDesc:TextView = itemView.findViewById(R.id.desc_visita)
        val rvSlotData:TextView = itemView.findViewById(R.id.data_visita)
        val rvFiles: RecyclerView = itemView.findViewById(R.id.rv_files)
        val rvButtonModifica: ImageView = itemView.findViewById(R.id.modifica_evento)
    }
}