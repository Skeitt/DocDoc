package com.example.docdoc.repository

import com.example.docdoc.model.Utente
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository {
    private val db = Firebase.firestore

    private val USERS_COLLECTION = "users"

    /** @brief funzione che aggiunge i dati di un utente al database Firestore
    * @param user utente che si vuole aggiungere alla collection "users"
    * @return true se l'aggiunta va a buon fine */
    fun addUserData(user: Utente): Task<Void> {
        return db.collection(USERS_COLLECTION).document(user.uid!!).set(user)
    }
}