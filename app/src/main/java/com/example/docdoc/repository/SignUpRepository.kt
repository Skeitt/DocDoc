package com.example.docdoc.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpRepository {
    private val firebaseAuth = Firebase.auth

    /** @brief funzione che registra un utente al servizio Authentication di Firebase
     * @param email La mail con cui viene registrato l'utente
     * @param password La password con cui viene registrato l'utente
     */
    fun signUp(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    /** @brief funzione che restituisce lo uid dell'utente loggato
     * @return String che indica lo uid dell'utente
     */
    fun getCurrentUserUid() : String
    {
        return firebaseAuth.currentUser?.uid!!
    }

    /** @brief funzione che restituisce la mail dell'utente loggato
     * @return String che indica la mail dell'utente loggato
     */
    fun getCurrentUserEmail() : String{
        return firebaseAuth.currentUser?.email!!
    }
}