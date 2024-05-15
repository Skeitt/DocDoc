package com.example.docdoc.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthRepository {
    private val firebaseAuth = Firebase.auth
    private lateinit var currentUser : FirebaseUser


    fun getCurrentUserUid() : String
    {
        currentUser = firebaseAuth.currentUser!!
        return currentUser.uid
    }
}