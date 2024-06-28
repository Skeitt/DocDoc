package com.example.docdoc.view.adapter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R

class FileListAdapter(private val fileList: List<String>, private val trashIcon: Boolean = false): RecyclerView.Adapter<FileListAdapter.ViewHolderClass>() {

    var onItemClick: ((String) -> Unit)? = null
    var onTrashClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return ViewHolderClass(itemView,trashIcon)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val fileItem = fileList[position]
        holder.fileName.text = fileItem

        holder.itemView.setOnClickListener{
            // al click dell'elemento viene restituito lo stesso al metodo onItemClick
            onItemClick?.invoke(fileItem)
        }

        holder.rvTrash.setOnClickListener{
            onTrashClick?.invoke(fileItem)
        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    class ViewHolderClass(itemView: View,trashIcon : Boolean): RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.filename)
        val rvTrash: ImageView = itemView.findViewById(R.id.img_trash_icon)
        init {
            if(trashIcon)
                rvTrash.visibility = View.VISIBLE
        }
    }
}