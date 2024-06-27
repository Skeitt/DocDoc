package com.example.docdoc.repository

import com.example.docdoc.model.Utente
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UtenteRepository {
    private val db = Firebase.firestore
    private val firebaseAuth = Firebase.auth

    private val USERS_COLLECTION = "users"
    private val CARTELLACLINICA_COLLECTION = "cartellaClinica"

    /** @brief funzione che restituisce i dati di un utente presenti nel database Firestore */
    fun getCurrentUser(): Task<DocumentSnapshot> {
        return  db.collection(USERS_COLLECTION).document(firebaseAuth.currentUser!!.uid).get()
    }

    /** @brief funzione che restituisce i dati di un utente presenti nel database Firestore
     * @param userId id dell'utente di cui si vogliono recuperare i dati */
    fun getUser(userId: String): Task<DocumentSnapshot> {
        return  db.collection(USERS_COLLECTION).document(userId).get()
    }

    /** @brief restituisce la lista dei pazienti di un determinato medico
     * @param uidMedico uid del medico di cui ottenere la lista dei pazienti
     */
    fun getListaPazienti(uidMedico: String): Query {
        return db.collection(USERS_COLLECTION)
            .whereEqualTo("medico", false)
            .whereEqualTo("uidMedico", uidMedico)
    }

    /** @brief funzione che aggiorna i dati dell'utente utente presenti nel database Firestore
     * @param userData contiene i nuovi dati dell'utente
     * se il documento già esiste il metodo set sovrascrive i dati*/
    fun updateCurrentUserData(userData: Utente): Task<Void> {
        return db.collection(USERS_COLLECTION).document(firebaseAuth.currentUser!!.uid).set(userData)
    }

    /** @brief funzione che elimina il profilo dell'utente corrente dal Database Firestore */
    fun deleteCurrentUser(): Task<Void> {
        return db.collection(USERS_COLLECTION).document(firebaseAuth.currentUser!!.uid).delete()
    }

    /** @brief funzione che elimina il profilo di autenticazione dell'utente corrente dal Database */
    fun deleteAuthenticationCurrentUser(): Task<Void> {
        return firebaseAuth.currentUser!!.delete()
    }

    /** @brief funzione che setta a null il campo: uidMedico dei pazienti nel momento in cui viene
     *  eliminato il profilo del dottore ad essi collegati */
    fun updatePatientsUidMedicoField(uidMedico: String): Task<Void> {
        //il batch ci consente di eseguire più operazioni di scrittura in Firestore come un'unica operazione
        val batch = db.batch()
        return db.collection(USERS_COLLECTION)
            .whereEqualTo("uidMedico", uidMedico)
            .get()
            //ottengo tutti i documenti in cui il campo uidMedico è uguale al valore passato come parametro alla funzione
            .continueWithTask { task ->
                if (task.isSuccessful && task.result != null) {
                    for (document in task.result!!) {
                        val docRef = document.reference
                        //aggiunge un'operazione al batch per aggiornare il campo uidMedico impostandolo a null
                        batch.update(docRef, "uidMedico", null)
                    }
                    //il batch di operazioni viene eseguito con batch.commit() che restituisce un Task<Void> che rappresenta l'operazione di commit
                    batch.commit()
                } else {
                    Tasks.forException(task.exception ?: Exception("Errore nel recuperare i Pazienti"))
                }
            }
    }

    /** @brief funzione che recupera il documento contenente le malattie e i farmaci del paziente
     * passato come parametro;
     * @param userId id dell'utente di cui vogliamo recuperare i dati */
    fun getCartellaClinica(userId: String): Task<DocumentSnapshot> {
        return db.collection(CARTELLACLINICA_COLLECTION).document(userId).get()
    }

    /** @brief funzione che aggiorna i dati della cartella clinica dell'utente presente nel database Firestore
     * @param userId id del paziente a cui viene aggiornata la cartella clinica
     * se il documento già esiste il metodo set sovrascrive i dati*/
    fun updateCartellaClinica(userId: String, cartellaClinicaData: Map<String, Any?>): Task<Void> {
        return db.collection(CARTELLACLINICA_COLLECTION).document(userId).set(cartellaClinicaData)
    }
}