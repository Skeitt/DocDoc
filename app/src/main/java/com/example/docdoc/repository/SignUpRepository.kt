package com.example.docdoc.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SignUpRepository {
    private val firebaseAuth = Firebase.auth

    suspend fun signUp(email: String, password: String): Boolean{
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentUserUid() : String
    {
        return firebaseAuth.currentUser!!.uid
    }
}