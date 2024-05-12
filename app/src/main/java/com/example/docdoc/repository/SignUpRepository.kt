package com.example.docdoc.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class SignUpRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String): Boolean{
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}