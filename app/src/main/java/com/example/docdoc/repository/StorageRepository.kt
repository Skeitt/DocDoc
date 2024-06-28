package com.example.docdoc.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.net.Uri
import com.google.android.gms.tasks.Task
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

    /** Funzione che elimina nello storage di Firebase tutti i file di un determinato evento, eliminando
     *  successivamente anche la cartella*/
    fun deleteAllFiles(eventId: String): Task<Void> {
        val folderRef = storage.reference.child("files/$eventId")
        return folderRef.listAll().continueWithTask { listResult ->
            //Per ogni file trovato nella cartella viene creato un Task per eliminarlo utilizzando
            // il metodo delete() su ogni riferimento di file
            // Il metodo map viene utilizzato per trasformare la lista di riferimenti ai file (items)
            // in una lista di Task di eliminazione
            val deleteTasks = listResult.result.items.map { it.delete() }
            //Crea un nuovo Task che sarà completato solo quando tutti i Task di eliminazione
            // individuali saranno completati. Questo Task aggregato è quello che viene restituito dalla funzione
            Tasks.whenAll(deleteTasks)
        }
    }
}