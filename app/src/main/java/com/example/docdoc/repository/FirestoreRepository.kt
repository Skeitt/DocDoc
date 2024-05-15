package com.example.docdoc.repository

import com.example.docdoc.model.Utente
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = Firebase.firestore

    private val USERS_COLLECTION = "users"

    suspend fun addUserData(user: Utente) : Boolean {
        val userMap = getUserMap(user)
        return try {
            db.collection(USERS_COLLECTION)
                .add(userMap)
                .await()
                true
        }
        catch(e : Exception)
        {
            false
        }
    }

    fun getUserMap(user: Utente) : HashMap<String,String?>
    {
        val userMap = hashMapOf(
            "uid" to user.uid,
            "nome" to user.nome,
            "cognome" to user.cognome,
            "cf" to user.codiceFiscale,
            "sesso" to user.sesso,
            "email" to user.email,
            "dataDiNascita" to user.dataDiNascita,
            "numDiTelefono" to user.numDiTelefono,
            "ruolo" to user.ruolo,
        )
        if (user.ruolo == "dottore") {
            userMap["indirizzoAmbulatorio"] = user.indirizzo
        } else {
            userMap["indirizzoDiResidenza"] = user.indirizzo
            userMap["uidMedico"] = user.uidMedico
        }
        return userMap
    }
}