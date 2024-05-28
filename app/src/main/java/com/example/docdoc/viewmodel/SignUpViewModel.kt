package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.SignUpRepository
import com.example.docdoc.uistate.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    // repository
    private val signUpRepository = SignUpRepository()
    private val firestoreRepository = FirestoreRepository()

    // LiveData per lo stato dell'UI
    // StateFlow per la gestione dello stato della registrazione
    private val _signupUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signupUiState.asStateFlow()

    // LiveData per l' e-mail
    private val _email = MutableLiveData<String>()
    val email : LiveData<String> get() = _email

    // LiveData per la password
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    fun signUp() {
        viewModelScope.launch {
            signUpRepository.signUp(_email.value!!, _password.value!!)
                .addOnSuccessListener {
                    /** Account creato con successo */
                    _signupUiState.value = SignUpUiState.authSuccess()
                }.addOnFailureListener {
                    /** Autenticazione non andata a buon fine */
                    _signupUiState.value = SignUpUiState.authError()
                }
        }
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }
}
