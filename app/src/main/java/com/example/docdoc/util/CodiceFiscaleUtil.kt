package com.example.docdoc.util

import java.util.Calendar

class CodiceFiscaleUtil {
    private val associazioneMesi = mapOf(
        "A" to "01",
        "B" to "02",
        "C" to "03",
        "D" to "04",
        "E" to "05",
        "H" to "06",
        "L" to "07",
        "M" to "08",
        "P" to "09",
        "R" to "10",
        "S" to "11",
        "T" to "12"
    )

    /** funzione che estrae la data di nascita da un codice fiscale.
        @param cf codice fiscale da cui estrarre la data di nascita
        @return restituisce uno String nel formato YYYY-MM-DD  */
    fun estraiDataDiNascita(cf: String) : String
    {
        var anno = cf.substring(6,8).toInt()
        anno += if (cf.substring(6,8).toInt() + 2000 <= Calendar.getInstance().get(Calendar.YEAR)) 2000 else 1900
        val mese = associazioneMesi[cf.substring(8,9)]
        var giorno = cf.substring(9,11).toInt()
        giorno = if (giorno < 40) giorno else (giorno - 40)
        return String.format("%04d-%02d-%02d", anno, mese, giorno)
    }

    /** funzione che estrae il sesso di una persona a partire dal codice fiscale.
        @param cf codice fiscale da cui estrarre il sesso
        @return restituisce "M" per il sesso Maschio e "F" per il sesso Femmina  */
    fun estraiSesso(cf : String) : String
    {
        return if (cf.substring(9,11).toInt()  > 31) "F" else "M"
    }
}