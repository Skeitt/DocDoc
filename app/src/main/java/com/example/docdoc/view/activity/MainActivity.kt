package com.example.docdoc.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityMainBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.view.fragment.FragmentHomePaziente
import com.example.docdoc.viewmodel.UtenteViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: UtenteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var utente: Utente? = null

        viewModel.getCurrentUser()
        lifecycleScope.launch{
            viewModel.uiState.collect{
                if(it.fetchData) {
                    utente = viewModel.currentUser.value
                    if (utente?.ruolo == "paziente")
                        impostaFragment(FragmentHomePaziente())
                }
            }
        }

        binding.menuInferiore.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (utente?.ruolo == "paziente")
                        impostaFragment(FragmentHomePaziente())
                }
                /*R.id.profilo -> {
                    if (utente?.ruolo == "paziente")
                        impostaFragment(FragmentProfiloMedico())
                }*/
                R.id.add_prenotazione -> {}
                else -> {}
            }
            true
        }
    }
    private fun impostaFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
