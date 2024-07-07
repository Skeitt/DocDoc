package com.example.docdoc.util

import com.example.docdoc.model.Prenotazione
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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

        fun creaSlotPrenotazioni(): ArrayList<Prenotazione> {
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

        /**
         * @brief data una lista di prenotazioni essa viene ordinata in base alla data
         */
        fun ordinaListaPerData(prenotazioni: ArrayList<Prenotazione>): ArrayList<Prenotazione> {
            // Utilizzo il metodo sortedBy per ordinare le prenotazioni in base alla data
            return ArrayList(prenotazioni.sortedBy { prenotazione ->
                prenotazione.data
            })
        }

        fun getIdUnivoco(prenotazione: Prenotazione) : String{
            val data = prenotazione.data
            val orario = prenotazione.orario
            val uidMedico = prenotazione.uidMedico
            return "${data}_${orario}_${uidMedico}"
        }

        fun calcolaSlotDisponibili(listaPrenotazioni : ArrayList<Prenotazione>, data : String) : ArrayList<Prenotazione>
        {
            var slotDisponibili: ArrayList<Prenotazione> = creaSlotPrenotazioni()
            val currentTime = LocalTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            for (prenotazione in listaPrenotazioni)
            {
                slotDisponibili.removeIf {
                    it.orario == prenotazione.orario
                }
            }

            // data di oggi
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = Date()
            val todayString = dateFormat.format(today)

            if(data == todayString){
                //se l'orario slot.orario è già passato rispetto all'orario corrente lo rimuovo dagli slot disponibili
                slotDisponibili.removeIf{slot ->
                    isTimeLessThan(slot.orario.toString(), currentTime.format(formatter).toString())
                }
            }

            return slotDisponibili
        }

        /** Funzione che controlla che l'orario: time1 sia passato rispetto all'orario: time2 */
        fun isTimeLessThan(time1: String, time2: String): Boolean {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val t1 = LocalTime.parse(time1, formatter)
            val t2 = LocalTime.parse(time2, formatter)
            return t1.isBefore(t2)
        }

    }
}