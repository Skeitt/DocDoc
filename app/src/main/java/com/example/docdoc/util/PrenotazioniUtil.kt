package com.example.docdoc.util

import com.example.docdoc.model.Prenotazione

class PrenotazioniUtil {
    companion object{
        val slotMatch: Map<String, Int> = mapOf(
            "09:00" to 0,
            "09:30" to 1,
            "10:00" to 2,
            "10:30" to 3,
            "11:00" to 4,
            "11:30" to 5,
            "12:00" to 6,
            "12:30" to 7,
            "14:00" to 8,
            "14:30" to 9,
            "15:00" to 10,
            "15:30" to 11,
            "16:00" to 12,
            "16:30" to 13,
        )

        fun creaListaPrenotazioni(): ArrayList<Prenotazione> {
            var lista = ArrayList<Prenotazione>()
            for (i in 9..12) {
                lista.add(Prenotazione(orario = String.format("%02d:00", i)))
                lista.add(Prenotazione(orario = String.format("%02d:30", i)))
            }
            for (i in 14..16) {
                lista.add(Prenotazione(orario = String.format("%02d:00", i)))
                lista.add(Prenotazione(orario = String.format("%02d:30", i)))
            }
            return ordinaListaPerOrario(lista)
        }

        /**
         * @brief data una lista di prenotazioni essa viene ordinata in base all'orario
         */
        fun ordinaListaPerOrario(prenotazioni: ArrayList<Prenotazione>): ArrayList<Prenotazione> {
            // Utilizzo il metodo sortedBy per ordinare le prenotazioni in base all'orario
            return ArrayList(prenotazioni.sortedBy { prenotazione ->
                // Divido l'orario in ore e minuti e converto in un valore Int per l'ordinamento
                val (ore, minuti) = prenotazione.orario!!.split(":")
                ore.toInt() * 60 + minuti.toInt()
            })
        }
    }
}