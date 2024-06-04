package com.example.docdoc.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


/** Classe che rappresenta il repository per la gestione del login */
class LoginRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun signIn(email: String, password: String): Boolean{
        val result: Boolean = try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
        return result
    }

    /** Funzione che ritorna true se l'utente è loggato e false altrimenti */
    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun logout(){
        return firebaseAuth.signOut()
    }
}