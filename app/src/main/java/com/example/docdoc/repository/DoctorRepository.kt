package com.example.docdoc.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorRepository {
    private val db = Firebase.firestore

    private val USERS_COLLECTION = "users"

    /**
    * @brief Questa funzione restituisce la lista dei medici registrati
     * @return rstituisce un document nel caso di successo oppure una exception
     */
    fun getDoctorList(): Task<QuerySnapshot> {
        return db.collection(USERS_COLLECTION).whereEqualTo("ruolo", "Medico").get()
    }
}