package com.example.docdoc.uistate

data class UtenteUiState(
    val fetchData: Boolean = false //true se i dati dell'utente sono stati recuperati
    ) {
        companion object{
            fun haveData() = UtenteUiState(fetchData = true)
        }
    }