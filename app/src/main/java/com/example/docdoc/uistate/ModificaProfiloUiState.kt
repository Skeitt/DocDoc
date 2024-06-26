package com.example.docdoc.uistate

data class ModificaProfiloUiState(
    val isModified: Boolean = false, //true se i dati dell'utente sono stati correttamente modificati
    val isEliminated: Boolean = false, //true se il profilo dell'utente è stato eliminato
    val isError: Boolean = false, //true se i dati dell'utente non sono stati correttamente modificati
) {
    //è un'istanza singola condivisa da tutte le istanze della classe
    companion object{
        fun modified() = ModificaProfiloUiState(isModified = true)
        fun eliminated()= ModificaProfiloUiState(isEliminated = true)

        fun error() = ModificaProfiloUiState(isError = true)
    }
}