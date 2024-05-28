package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.databinding.ActivityRegistratiBinding
import com.example.docdoc.util.InputValidator
import com.example.docdoc.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistratiBinding
    private val viewModel: SignUpViewModel by viewModels()
    private val inputValidator = InputValidator()

    private val EMAIL_ERROR = "Formato email non valido"
    private val PASSWORD_ERROR = "Formato password non valido\nLa password deve contenere almeno un carattere maiuscolo, minuscolo,un numero ed un carattere speciale tra @, $, %, ^, &, +, = e .\nInoltre deve contentere almeno 8 caratteri"
    private val PASSWORD_MATCHING_ERROR  = "Le password non corrispondono"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // viewBinding
        binding = ActivityRegistratiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //  Registrazione dell'utente
        binding.buttonContinua.setOnClickListener(onContinua())
        //  Ritorno al Login
        binding.buttonIndietro.setOnClickListener(goToLogin())

        // controllo dello stato della registrazione
        /** Lo stato della registrazione è diviso in:
         *  - errore di registrazione: l'autenticazione (email+password) non è andata a buon fine
         *  - errore nel push dei dati: la push dei dati dell'utente non è stata eseguita correttamente
         *  - registrazione (email + password) e push dei dati effettuati correttamente
         */
        lifecycleScope.launch {
            viewModel.signUpUiState.collect{stato ->
                Log.d("SIGNUPUISTATE", "isSignedUp: " + stato.isSignedUp.toString() + "\nisError: " + stato.isError.toString())
                if(stato.isError)
                {
                    Toast.makeText(this@SignUpActivity, "Registrazione fallita",Toast.LENGTH_SHORT).show()
                }
                if(stato.isSignedUp)
                {
                    /** Quando si è registrati si fa la push dei dati compilati sul database */
                    startActivity(Intent(this@SignUpActivity, FormActivity::class.java))
                    finish()
                }
            }
        }

        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editEmail.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }
        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editPw.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }
    }

    private fun onContinua(): View.OnClickListener? {
        return View.OnClickListener {
            if (!inputValidator.isValidEmail(binding.editEmail.text.toString())) {
                binding.editEmail.error = EMAIL_ERROR
            } else if (!inputValidator.isValidPassword(binding.editPw.text.toString())) {
                binding.editPw.error = PASSWORD_ERROR
            } else if (binding.editPw.text.toString() != binding.confPw.text.toString()) {
                binding.confPw.error = PASSWORD_MATCHING_ERROR
            } else {
                viewModel.signUp()
            }
        }
    }

    private fun goToLogin(): View.OnClickListener? {
        // si torna all'activity precedente ovvero il Login
        return View.OnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
    }
}