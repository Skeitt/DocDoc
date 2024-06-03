package com.example.docdoc.repository

import com.example.docdoc.model.Utente
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UtenteRepository {
    private val db = Firebase.firestore
    private val firebaseAuth = Firebase.auth

    private val USERS_COLLECTION = "users"

    /** @brief funzione che restituisce i dati di un utente presenti nel database Firestore */
    fun getCurrentUser(): Task<DocumentSnapshot> {
        return  db.collection(USERS_COLLECTION).document(firebaseAuth.currentUser!!.uid).get()
    }

    /** @brief funzione che restituisce i dati di un utente presenti nel database Firestore
     * @param userId id dell'utente di cui si vogliono recuperare i dati */
    fun getUser(userId: String): Task<DocumentSnapshot> {
        return  db.collection(USERS_COLLECTION).document(userId).get()
    }

    fun getListaPazienti(): Query {
        return db.collection(USERS_COLLECTION)
            .whereEqualTo("ruolo", "Paziente")
    }
}