package com.example.docdoc.model

data class Prenotazione(
    val pid: String? = null,
    var uidPaziente: String? = null,
    var data: String? = null,
    var orario: String? = null,
    var descrizione: String? = null,
)
