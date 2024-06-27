package com.example.docdoc.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.UploadTask
import java.io.File


class StorageRepository {

    val storage = Firebase.storage


    fun uploadFile(file : Uri, eventId : String, filename: String): UploadTask {
        return storage.reference.child("files/$eventId/$filename").putFile(file)
    }

    fun downloadFile(eventId: String, filename: String, localFile: File): FileDownloadTask {
        return storage.reference.child("files/$eventId/$filename").getFile(localFile)
    }
}