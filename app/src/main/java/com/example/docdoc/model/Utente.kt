package com.example.docdoc.model

/**
 * @param dataDiNascita indica la data di nascita dell'utente nel formato YY-MM-DD
 */
data class Utente(
    var uid: String? = null,
    var nome: String? = null,
    var cognome: String? = null,
    var sesso: String? = null,
    var email: String? = null,
    var codiceFiscale: String? = null,
    var dataDiNascita: String? = null,
    var numDiTelefono: String? = null,
    var medico: Boolean? = null,
    var indirizzo: String? = null, // per il medico è dell'ambulatorio e per il paziente di residenza

    // solo paziente
    var uidMedico: String? = null, // uid del medico associato al paziente
)