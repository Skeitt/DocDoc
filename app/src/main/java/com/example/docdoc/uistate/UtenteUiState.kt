package com.example.docdoc.uistate

data class UtenteUiState(
    val fetchData: Boolean = false
    ) {
        companion object{
            fun haveData() = UtenteUiState(fetchData = true)
        }
    }