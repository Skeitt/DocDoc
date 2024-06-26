package com.example.docdoc.repository

import com.example.docdoc.model.Evento
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventoRepository {

    private val db = Firebase.firestore

    private val EVENTS_COLLECTION = "events"

    /** @brief funzione che inserisce i dati relativi all'evento di un paziente nel database Firestore
     * @param eventData contiene i dati relativi all'evento */
    fun setEventData(eventData: Evento): Task<Void> {
        return db.collection(EVENTS_COLLECTION).document(eventData.eid!!).set(eventData)
    }

}