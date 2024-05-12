package com.example.docdoc.uistate

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isError: Boolean = false,
){
    //è un'istanza singola condivisa da tutte le istanze della classe
    companion object{
        fun loading() = LoginUiState(isLoading = true)
        fun loggedIn() = LoginUiState(isLoggedIn = true)
        fun error() = LoginUiState(isError = true)
    }
}