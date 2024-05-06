package com.example.docdoc.model

data class Medico(
    val uid: String? = null,
    var nome: String? = null,
    var cognome: String? = null,
    var cf: String? = null,
    var dataDiNascita: String? = null,
    var numDiTelefono: String? = null,
    var indirizzoAmbulatorio: String? = null,
    var numDiTelefonoAmbulatorio: String? = null
)
