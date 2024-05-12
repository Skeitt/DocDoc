package com.example.docdoc.model

data class Utente(
    val uid: String = "",
    var nome: String? = null,
    var cognome: String? = null,
    var sesso: String? = null,
    var email: String = "",
    var codiceFiscale: String? = null,
    var dataDiNascita: String? = null,
    var numDiTelefono: String? = null,
    var ruolo: String?  = null,
    var indirizzo: String? = null, // per il medico è dell'ambulatorio e per il paziente di residenza

    // solo paziente
    var uidMedico: String? = null, // uid del medico associato al paziente
)