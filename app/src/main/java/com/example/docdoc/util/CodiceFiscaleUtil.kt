package com.example.docdoc.util

import java.util.Calendar

class CodiceFiscaleUtil {
    companion object{
        private val associazioneMesi = mapOf(
            "A" to 1,
            "B" to 2,
            "C" to 3,
            "D" to 4,
            "E" to 5,
            "H" to 6,
            "L" to 7,
            "M" to 8,
            "P" to 9,
            "R" to 10,
            "S" to 11,
            "T" to 12
        )
        // indica l'offset tra il giorno di nascita di un Uomo o di una Donna
        private const val OFFSET_F = 40
        /** @brief funzione che estrae la data di nascita da un codice fiscale.
         * @param codiceFiscale codice fiscale da cui estrarre la data di nascita
         * @return restituisce uno String nel formato YYYY-MM-DD  */
        fun estraiDataDiNascita(codiceFiscale: String): String {
            val cf = codiceFiscale.uppercase()
            var anno = cf.substring(6, 8).toInt()
            anno += if (cf.substring(6, 8).toInt() + 2000 <= Calendar.getInstance()
                    .get(Calendar.YEAR)
            ) 2000 else 1900
            val mese = associazioneMesi[cf.substring(8, 9)]
            var giorno = cf.substring(9, 11).toInt()
            giorno = if (giorno < OFFSET_F) giorno else (giorno - OFFSET_F)
            return String.format("%04d-%02d-%02d", anno, mese, giorno)
        }

        /** @brief funzione che estrae il sesso di una persona a partire dal codice fiscale.
         * @param cf codice fiscale da cui estrarre il sesso
         * @return restituisce "M" per il sesso Maschio e "F" per il sesso Femmina  */
        fun estraiSesso(cf: String): String {
            return if (cf.substring(9, 11).toInt() > 31) "F" else "M"
        }
    }
}