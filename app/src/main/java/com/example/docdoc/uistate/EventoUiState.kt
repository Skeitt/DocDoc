package com.example.docdoc.uistate

data class EventoUiState(
    val fetchData: Boolean = false, //true se i dati dell'evento sono stati recuperati
    val isCreated: Boolean = false, //true se l'evento è stato creato o modificato e aggiunto al Database
    val isEliminated: Boolean = false, //true se l'evento è stato eliminato
    val isError: Boolean = false //true se c'è stato un errore
) {

    //è un'istanza singola condivisa da tutte le istanze della classe
    companion object{
        fun haveData() = EventoUiState(fetchData = true)
        fun created() = EventoUiState(isCreated = true)
        fun eliminated()= EventoUiState(isEliminated = true)
        fun error() = EventoUiState(isError = true)
    }
}