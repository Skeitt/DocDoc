package com.example.docdoc.uistate

data class ModificaMalattieFarmaciUiState(
    val isModified: Boolean = false, //true se i dati dell'utente sono stati correttamente modificati
    val isError: Boolean = false, //true se i dati dell'utente non sono stati correttamente modificati
) {
    //è un'istanza singola condivisa da tutte le istanze della classe
    companion object{
        fun modified() = ModificaMalattieFarmaciUiState(isModified = true)

        fun error() = ModificaMalattieFarmaciUiState(isError = true)
    }
}