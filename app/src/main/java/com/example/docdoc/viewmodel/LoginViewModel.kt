package com.example.docdoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.repository.LoginRepository
import com.example.docdoc.uistate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()

    // StateFlow per la gestione dello stato del login
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /** Funzione che gestisce lo stato del login */
    fun login(email: String, password: String){
        viewModelScope.launch {
            _uiState.value = LoginUiState.loading()
            val access = repository.signIn(email, password)
            if (access) {
                _uiState.value = LoginUiState.loggedIn()
            } else {
                _uiState.value = LoginUiState.error()
            }
        }
    }

    /** Funzione che ritorna true se l'utente è loggato e false altrimenti */
    fun isLoggedIn(): Boolean {
        return repository.isLoggedIn()
    }
}