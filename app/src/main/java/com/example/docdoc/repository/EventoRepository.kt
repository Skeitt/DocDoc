package com.example.docdoc.repository

import com.example.docdoc.model.Evento
import com.example.docdoc.model.Utente
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
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

    /** @brief funzione che restituisce i dati di un Evento presenti nel database Firestore */
    fun getEventData(eventID: String): Task<DocumentSnapshot> {
        return  db.collection(EVENTS_COLLECTION).document(eventID).get()
    }

    /** @brief funzione che aggiorna i dati dell'evento presenti nel database Firestore
     * @param evento contiene i nuovi dati dell'evento
     * se il documento già esiste il metodo set sovrascrive i dati*/
    fun updateEventData(evento: Evento): Task<Void> {
        return db.collection(EVENTS_COLLECTION).document(evento.eid!!).set(evento)
    }

    /** @brief funzione che elimina l'evento dal Database Firestore
      * @param eventID id dell'evento che si vuole eliminare */
    fun deleteEvent(eventID: String): Task<Void> {
        return db.collection(EVENTS_COLLECTION).document(eventID).delete()
    }

    fun generateId(): String{
        // Get the collection reference
        val collectionRef = db.collection(EVENTS_COLLECTION);

        // Generate "locally" a new document for the given collection reference
        val docRef = collectionRef.document()

        // Get the new document Id
        return docRef.id;
    }

}