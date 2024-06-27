package com.example.docdoc.model

data class Evento(
    var eid: String? = null,
    var uidPaziente: String? = null,
    var motivo: String? = null,
    var data: String? = null,
    var descrizione: String? = null,
    var listaFile: ArrayList<String>? = null // lista di stringhe che indicano gli URL dei vari file appartenenti all'evento
)
