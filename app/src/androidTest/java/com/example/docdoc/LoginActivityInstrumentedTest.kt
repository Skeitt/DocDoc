package com.example.docdoc

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.docdoc.view.activity.LoginActivity
import com.example.docdoc.viewmodel.UtenteViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityInstrumentedTest {
    private lateinit var email1 : String
    private lateinit var password1 : String
    private lateinit var email2 : String
    private lateinit var password2 : String
    private lateinit var viewmodel : UtenteViewModel

    //regola che viene utilizzata per avviare un'attività
    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity>
            = ActivityScenarioRule(LoginActivity::class.java)

    //I dati vengono inizializzati
    @Before
    fun setUp() {
        email1 = "prova1@gmail.com"
        password1 = "Provaprova.1"
        email2 = "prova2@gmail.com"
        password2 = "Provaprova.2"
        viewmodel = UtenteViewModel()
    }

    /** Questo Test andrà a buon fine perchè l'utente email1 password1 è registrato nel database*/
    @Test
    fun testLogin1() {
        //inserimento email
        Espresso.onView(withId(R.id.edit_email))
            .perform(ViewActions.typeText(email1), ViewActions.closeSoftKeyboard())
        //inserimento password
        Espresso.onView(withId(R.id.edit_pw))
            .perform(ViewActions.typeText(password1), ViewActions.closeSoftKeyboard())
        //click sul button accedi
        Espresso.onView(withId(R.id.accedi_button)).perform(ViewActions.click())

        Thread.sleep(4000)

        //se viene aperta la MainActivity, quindi l'utente è loggato, il test è andato a buon fine altrimenti è fallito
        Espresso.onView(withId(R.id.main))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewmodel.logout()
    }

    /** Questo Test non andrà a buon fine perchè l'utente email2 password2 non è registrato nel database*/
    @Test
    fun testLogin2() {
        //inserimento email
        Espresso.onView(withId(R.id.edit_email))
            .perform(ViewActions.typeText(email2), ViewActions.closeSoftKeyboard())
        //inserimento password
        Espresso.onView(withId(R.id.edit_pw))
            .perform(ViewActions.typeText(password2), ViewActions.closeSoftKeyboard())
        //click sul button accedi
        Espresso.onView(withId(R.id.accedi_button)).perform(ViewActions.click())

        Thread.sleep(5000)

        //se viene aperta la MainActivity, quindi l'utente è loggato, il test è andato a buon fine altrimenti è fallito
        Espresso.onView(withId(R.id.main))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}