package com.example.docdoc.view.adapter

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R

class FileListAdapter(private val fileList: List<String>, private val trashIcon: Boolean = false): RecyclerView.Adapter<FileListAdapter.ViewHolderClass>() {

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val fileItem = fileList[position]
        holder.fileName.text = fileItem

        holder.itemView.setOnClickListener{
            // al click dell'elemento viene restituito lo stesso al metodo onItemClick
            onItemClick?.invoke(fileItem)
        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val fileName: TextView = itemView.findViewById(R.id.filename)
    }

    fun getFileNameFromUri(context: Context, uri: Uri): kotlin.String? {
        var fileName: kotlin.String? = null

        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
        } else if (uri.scheme == ContentResolver.SCHEME_FILE) {
            fileName = uri.lastPathSegment
        }

        return fileName
    }
}