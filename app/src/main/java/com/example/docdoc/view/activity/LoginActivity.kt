package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.databinding.ActivityAccediBinding
import com.example.docdoc.util.InputValidator
import com.example.docdoc.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


/** Activity per il login */
class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAccediBinding
    private val viewModel: LoginViewModel by viewModels()
    private val inputValidator = InputValidator()

    private val EMAIL_ERROR = "Email non valida"
    private val PASSWORD_ERROR = "Password non valida"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAccediBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewBinding.accediButton.setOnClickListener(loginOnClick())
        viewBinding.registratiButton.setOnClickListener(signUpOnClick())

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoggedIn) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                if (it.isError) {
                    Toast.makeText(this@LoginActivity, "Errore nel Login!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    /** nel momento in cui lancio l'activity se l'utente è già loggato lancio la Main Activity */
    override fun onStart() {
        super.onStart()
        if (viewModel.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /** al click del pulsante: entra viene effettuato il login controllando la correttezza dei campi */
    private fun loginOnClick(): View.OnClickListener {
        return View.OnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val password = viewBinding.editPw.text.toString()
            if (inputValidator.isValidEmail(email)) {
                if (inputValidator.isValidPassword(password)) {
                    viewModel.login(email, password)
                } else {
                    viewBinding.editPw.error = PASSWORD_ERROR
                }
            } else {
                viewBinding.editEmail.error = EMAIL_ERROR
            }
        }
    }

    /** al click del pulsante: registrati viene aperta l'activity della registrazione */
    private fun signUpOnClick(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

}