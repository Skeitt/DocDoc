package com.example.docdoc

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.example.docdoc.view.activity.LoginActivity
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Before
import org.junit.Test


class PrenotazioneActivityInstrumentedTest {
    private lateinit var email1 : String
    private lateinit var password1 : String
    private lateinit var nome : String
    private lateinit var cognome : String
    private lateinit var ora : String
    private lateinit var descrizione : String


    //funzione utilizzata per inizializzare i dati
    @Before
    fun setUp() {
        //l'utente che inserisce la prenotazione è un medico
        email1 = "prova1@gmail.com"
        password1 = "Provaprova.1"
        nome = "ProvaPrenotazione"
        cognome = "ProvaPrenotazione"
        ora = "15:00"
        descrizione = "Descrizione ProvaPrenotazione"
    }

    /** Questo Test ha il compito di inserire una nuova prenotazione all'interno del database */
    @Test
    fun testAddPrenotazione() {
        //viene lanciata l'activity di login//viene lanciata l'activity di login
        ActivityScenario.launch(LoginActivity::class.java)
        //inserimento mail
        Espresso.onView(ViewMatchers.withId(R.id.edit_email))
            .perform(ViewActions.typeText(email1), ViewActions.closeSoftKeyboard())
        //inserimento password
        Espresso.onView(ViewMatchers.withId(R.id.edit_pw))
            .perform(ViewActions.typeText(password1), ViewActions.closeSoftKeyboard())
        //clik sul button Accedi
        Espresso.onView(ViewMatchers.withId(R.id.accedi_button)).perform(ViewActions.click())

        Thread.sleep(4000)

        //click sul pulsante per aggiungere una nuova prenotazione
        Espresso.onView(ViewMatchers.withId(R.id.add_prenotazione)).perform(ViewActions.click())

        Thread.sleep(2000)

        //inserimento nome
        Espresso.onView(ViewMatchers.withId(R.id.edit_nome))
            .perform(ViewActions.typeText(nome), ViewActions.closeSoftKeyboard())
        //inserimento cognome
        Espresso.onView(ViewMatchers.withId(R.id.edit_cognome))
            .perform(ViewActions.typeText(cognome), ViewActions.closeSoftKeyboard())
        //click sul button per inserire la data
        Espresso.onView(ViewMatchers.withId(R.id.button_editData)).perform(ViewActions.click())
        //setto la data corrente nel DatePickerDialog
        Espresso.onView(withText("OK")).perform(ViewActions.click())
        //click sul button per inserire l'ora
        Espresso.onView(ViewMatchers.withId(R.id.button_editOra)).perform(ViewActions.click())
        //setto l'ora prestabilita nel AlertDialog
        Espresso.onView(withText(ora)).perform(ViewActions.click())
        //inserimento descrizione
        Espresso.onView(ViewMatchers.withId(R.id.edit_descrizione))
            .perform(ViewActions.typeText(descrizione), ViewActions.closeSoftKeyboard())
        //click sul button aggiungi prenotazione quindi continua
        Espresso.onView(ViewMatchers.withId(R.id.button_continua)).perform(ViewActions.click())
        //click sul button indietro per tornare alla home
        Espresso.onView(ViewMatchers.withId(R.id.button_indietro)).perform(ViewActions.click())

        Thread.sleep(2000)
        //Se ritorno alla home il test è andato a buon fine altrimenti è fallito
        Espresso.onView(ViewMatchers.withId(R.id.main))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}