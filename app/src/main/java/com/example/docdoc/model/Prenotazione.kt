package com.example.docdoc.model

/**
 * @param data indica la data della prenotazione nel formato YY-MM-DD
 */
data class Prenotazione(
    val pid: String? = null,
    var uidPaziente: String? = null,
    var nomePaziente: String? = null,
    var cognomePaziente: String? = null,
    var data: String? = null,
    var orario: String? = null,
    var descrizione: String? = null,
)
