package com.example.docdoc.model

/**
 * @param data indica la data della prenotazione nel formato YY-MM-DD
 * @param uidPaziente uid dell'utente che effettua la prenotazione (medico o paziente)
 */
data class Prenotazione(
    var pid: String? = null,
    var uidPaziente: String? = null,
    var uidMedico: String? = null,
    var nomePaziente: String? = null,
    var cognomePaziente: String? = null,
    var data: String? = null,
    var orario: String? = null,
    var descrizione: String? = null,
)
