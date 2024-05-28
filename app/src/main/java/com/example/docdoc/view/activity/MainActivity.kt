package com.example.docdoc.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityMainBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.view.fragment.FragmentHomePaziente
import com.example.docdoc.view.fragment.FragmentProfiloMedico
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

        var currentUser: Utente? = null

        viewModel.getCurrentUser()
        lifecycleScope.launch{
            viewModel.uiState.collect{
                if(it.fetchData) {
                    currentUser = viewModel.currentUser.value
                    if (currentUser?.ruolo == "paziente") {
                        binding.buttonProfiloMedico.visibility = View.VISIBLE
                        binding.buttonProfiloMedico.setOnClickListener(goToDoctorProfile(currentUser?.uidMedico!!))
                        impostaFragment(FragmentHomePaziente())
                    }
                }
            }
        }

        binding.menuInferiore.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (currentUser?.ruolo == "paziente")
                        binding.buttonProfiloMedico.visibility = View.VISIBLE
                        impostaFragment(FragmentHomePaziente())
                }
                /*R.id.profilo -> {
                    if (currentUser?.ruolo == "paziente")
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

    private fun goToDoctorProfile(uidMedico: String): View.OnClickListener?{
        return View.OnClickListener {
            viewModel.getUser(uidMedico)
            impostaFragment(FragmentProfiloMedico())
        }
    }
}
