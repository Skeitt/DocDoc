package com.example.docdoc
import com.example.docdoc.model.Prenotazione
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test

class PrenotazioneUnitTest {

    /**Funzione che testa l'inserimento, la modifica e l'eliminazione di una Prenotazione */
    @Test
    fun testPrenotazioneLifecycle() {
        // Creiamo una lista di prenotazioni vuota
        val prenotazioniList = mutableListOf<Prenotazione>()

        // Creiamo una nuova prenotazione
        val newPrenotazione = Prenotazione(
            pid = "2024-07-15_10:00_medicoUserId",
            uidPaziente = "pazienteUserId",
            uidMedico = "medicoUserId",
            nomePaziente = "Mario",
            cognomePaziente = "Rossi",
            data = "2024-07-15",
            orario = "10:00",
            descrizione = "Visita di controllo"
        )

        // Aggiungiamo la prenotazione alla lista
        prenotazioniList.add(newPrenotazione)

        // Verifichiamo che la lista non sia vuota e contenga la prenotazione
        assertNotNull(prenotazioniList)
        assertEquals(1, prenotazioniList.size)

        // Verifichiamo che la prenotazione inserita sia corretta
        val insertedPrenotazione = prenotazioniList[0]
        assertEquals("2024-07-15_10:00_medicoUserId", insertedPrenotazione.pid)
        assertEquals("pazienteUserId", insertedPrenotazione.uidPaziente)
        assertEquals("medicoUserId", insertedPrenotazione.uidMedico)
        assertEquals("Mario", insertedPrenotazione.nomePaziente)
        assertEquals("Rossi", insertedPrenotazione.cognomePaziente)
        assertEquals("2024-07-15", insertedPrenotazione.data)
        assertEquals("10:00", insertedPrenotazione.orario)
        assertEquals("Visita di controllo", insertedPrenotazione.descrizione)

        // Modifichiamo la prenotazione
        insertedPrenotazione.data = "2024-07-20"
        insertedPrenotazione.orario = "11:00"
        insertedPrenotazione.descrizione = "Visita di controllo posticipata"

        // Verifichiamo che la prenotazione sia stata modificata correttamente
        assertEquals("2024-07-20", insertedPrenotazione.data)
        assertEquals("11:00", insertedPrenotazione.orario)
        assertEquals("Visita di controllo posticipata", insertedPrenotazione.descrizione)

        // Rimuoviamo la prenotazione dalla lista
        prenotazioniList.remove(insertedPrenotazione)

        // Verifichiamo che la lista sia vuota, quindi non ci sia la prenotazione con quello specifico pid
        assertEquals(0, prenotazioniList.size)
        assertNull(prenotazioniList.find { it.pid == "2024-07-15_10:00_medicoUserId" })
    }
}