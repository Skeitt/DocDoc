package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.docdoc.databinding.ActivityAccediBinding
import com.example.docdoc.viewmodel.LoginViewModel
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.util.InputValidator
import kotlinx.coroutines.launch

/** Activity per il login */
class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAccediBinding
    private val viewModel: LoginViewModel by viewModels()
    private val inputValidator = InputValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAccediBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewBinding.accediButton.setOnClickListener(loginOnClick())
        viewBinding.registratiButton.setOnClickListener(signUpOnClick())

        lifecycleScope.launch {
            viewModel.uiState.collect {
                if (it.isLoading){
                    //disabilito l'interfaccia utente
                    disableUI()
                } else {
                    //abilito l'inerfaccia utente
                    enableUI()
                }
                if (it.isLoggedIn){
                    startActivity( Intent(this@LoginActivity,MainActivity::class.java) )
                    finish()
                }
                if (it.isError){
                    enableUI()
                    Toast.makeText(this@LoginActivity, "Errore nel Login!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun disableUI(){
        viewBinding.editEmail.isEnabled = false
        viewBinding.editPw.isEnabled = false
        viewBinding.accediButton.isEnabled = false
        viewBinding.registratiButton.isEnabled = false
    }

    private fun enableUI(){
        viewBinding.editEmail.isEnabled = true
        viewBinding.editPw.isEnabled = true
        viewBinding.accediButton.isEnabled = true
        viewBinding.registratiButton.isEnabled = true
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
    private fun loginOnClick(): View.OnClickListener{
        return View.OnClickListener {
            val email = viewBinding.editEmail.text.toString()
            val password = viewBinding.editPw.text.toString()
            if(inputValidator.isValidEmail(email)){
                if(inputValidator.isValidPassword(password)){
                    viewModel.login(email, password)
                }else{
                    Toast.makeText(this, "Password non valida", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Email non valida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** al click del pulsante: registrati viene aperta l'activity della registrazione */
    private fun signUpOnClick(): View.OnClickListener{
        return View.OnClickListener {
            //startActivity( Intent(this, SignUpActivity::class.java) )
        }
    }

}