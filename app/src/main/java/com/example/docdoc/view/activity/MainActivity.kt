package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityMainBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.view.fragment.FragmentHome
import com.example.docdoc.view.fragment.FragmentProfiloMedico
import com.example.docdoc.view.fragment.FragmentProfiloPaziente
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
        //viewModel.fetchMalattieFarmaciPaziente(currentUser?.uid!!)
        lifecycleScope.launch {
            viewModel.dataUiState.collect {
                if (it.fetchData) {
                    currentUser = viewModel.currentUser.value
                    impostaFragment(FragmentHome())
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loginUiState.collect {
                if(!it.isLoggedIn){
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

        binding.menuInferiore.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                        impostaFragment(FragmentHome())
                }

                R.id.profilo -> {
                    if (currentUser?.medico!!) {
                        impostaFragment(FragmentProfiloMedico())
                    }else{
                        impostaFragment(FragmentProfiloPaziente())
                    }
                }

                R.id.add_prenotazione -> {}
                else -> {}
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        //se viene riaperta la MainActivity riaggiorno il contenuto del viewModel.currentUser
        viewModel.getCurrentUser()
    }

    private fun impostaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
