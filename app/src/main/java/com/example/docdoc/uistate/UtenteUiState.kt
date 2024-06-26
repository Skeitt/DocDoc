package com.example.docdoc.uistate

data class UtenteUiState(
    val fetchData: Boolean = false, //true se i dati dell'utente sono stati recuperati
    val isError: Boolean = false //true se c'è stato un errore durante il recupero dei dati dell'utente
    ) {
        companion object{
            fun haveData() = UtenteUiState(fetchData = true)
            fun error() = UtenteUiState(isError = true)
        }
    }