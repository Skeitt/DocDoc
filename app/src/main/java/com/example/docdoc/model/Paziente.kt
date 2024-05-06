package com.example.docdoc.model

data class Paziente (
    val uid: String? = null,
    var nome: String? = null,
    var cognome: String? = null,
    var cf: String? = null,
    var dataDiNascita: String? = null,
    var numDiTelefono: String? = null,
    var indirizzoDiResidenza: String? = null,
    var uidMedico: String? = null, // uid del medico associato al paziente
)
