package com.example.docdoc.model

data class Evento (
    val eid: String? = null,
    var motivo: String? = null,
    var data: String? = null,
    var descrizione: String? = null,
    val listaFile: List<String>? = null, // lista di stringhe che indicano gli URL dei vari file appartenenti all'evento
)
