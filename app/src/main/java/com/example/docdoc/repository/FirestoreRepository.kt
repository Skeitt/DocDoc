package com.example.docdoc.repository

import com.example.docdoc.model.Utente
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository {
    val db = Firebase.firestore

    private val USERS_COLLECTION = "users"
    private val BOOKING_COLLECTION = "prenotazioni"

    /** @brief funzione che aggiunge i dati di un utente al database Firestore
    * @param user utente che si vuole aggiungere alla collection "users"
    * @return true se l'aggiunta va a buon fine */
    fun addUserData(user: Utente): Task<Void> {
        return db.collection(USERS_COLLECTION).document(user.uid!!).set(user)
    }

    /**
     * @brief Questa funzione restituisce la lista dei medici registrati
     * @return rstituisce un document nel caso di successo oppure una exception
     */
    fun getDoctorList(): Query {
        return db.collection(USERS_COLLECTION)
            .whereEqualTo("medico", true)
    }

    /**
     * @brief Questa funzione effettua una query per vedere se presente un documento con id specificato
     * @return restituisce un documento richiesto
     */
    fun isInfoStored(uid: String): Task<DocumentSnapshot> {
        return db.collection(USERS_COLLECTION).document(uid).get()
    }

    /**
     * @brief Questa funzione effettua una query per ottenere tutte le prenotazioni in un determinato giorno
     * @param giorno indica il giorno della prenotazione
     * @return Restituisce un oggetto di tipo Query
     */
    fun getPrenotazioniPerGiorno(giorno: String): Query {
        return db.collection(BOOKING_COLLECTION)
            .whereEqualTo("data", giorno)
    }
}