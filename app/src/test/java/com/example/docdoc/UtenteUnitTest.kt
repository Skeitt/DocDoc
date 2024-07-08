package com.example.docdoc
import com.example.docdoc.model.Utente
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
class UtenteUnitTest {

    /**Funzione che testa l'inserimento di un Utente */
    @Test
    fun testAddUserToList() {
        // Creiamo una lista di utenti vuota
        val userList = mutableListOf<Utente>()

        // Creiamo un nuovo utente
        val newUser = Utente(
            uid = "mariorossi19800101",
            nome = "Mario",
            cognome = "Rossi",
            sesso = "M",
            email = "mario.rossi@example.com",
            codiceFiscale = "MRARSS80A01H501U",
            dataDiNascita = "1980-01-01",
            numDiTelefono = "1234567890",
            medico = false,
            indirizzo = "Via Roma, 1, 65100, Pescara",
            uidMedico = "54321868968"
        )

        // Aggiungiamo l'utente alla lista
        userList.add(newUser)

        // Verifichiamo che la lista non sia vuota
        assertNotNull(userList)
        //Verifichiamo che nella lista ci sia un solo utente
        assertEquals(1, userList.size)

        // Verifichiamo che l'utente inserito sia corretto
        val insertedUser = userList[0]
        assertEquals("mariorossi19800101", insertedUser.uid)
        assertEquals("Mario", insertedUser.nome)
        assertEquals("Rossi", insertedUser.cognome)
        assertEquals("M", insertedUser.sesso)
        assertEquals("mario.rossi@example.com", insertedUser.email)
        assertEquals("MRARSS80A01H501U", insertedUser.codiceFiscale)
        assertEquals("1980-01-01", insertedUser.dataDiNascita)
        assertEquals("1234567890", insertedUser.numDiTelefono)
        assertEquals(false, insertedUser.medico)
        assertEquals("Via Roma, 1, 65100, Pescara", insertedUser.indirizzo)
        assertEquals("54321868968", insertedUser.uidMedico)
    }
}