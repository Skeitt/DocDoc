package com.example.docdoc.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SignUpRepository {
    private val firebaseAuth = Firebase.auth

    /** @brief funzione che registra un utente al servizio Authentication di Firebase
     * @param email La mail con cui viene registrato l'utente
     * @param password La password con cui viene registrato l'utente
     */
    suspend fun signUp(email: String, password: String): Boolean{
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    /** @brief funzione che restituisce lo uid dell'utente loggato
     * @return String che indica lo uid dell'utente
     */
    fun getCurrentUserUid() : String
    {
        return firebaseAuth.currentUser!!.uid
    }
}