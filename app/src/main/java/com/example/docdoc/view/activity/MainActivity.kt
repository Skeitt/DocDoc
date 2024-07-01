package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityMainBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.view.fragment.FragmentHome
import com.example.docdoc.view.fragment.FragmentListaPrenotazioni
import com.example.docdoc.view.fragment.FragmentProfiloMedico
import com.example.docdoc.view.fragment.FragmentProfiloPaziente
import com.example.docdoc.viewmodel.UtenteViewModel
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    private val viewModel: UtenteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var currentUser: Utente? = null


        lifecycleScope.launch {
            viewModel.getCurrentUser()
            viewModel.dataUiState.collect {
                if (it.fetchData) {
                    currentUser = viewModel.currentUser.value
                    impostaFragment(FragmentHome())
                }
                if (it.isError){
                    //visualizzo un messaggio di errore
                    Toast.makeText(this@MainActivity, "Errore", Toast.LENGTH_SHORT)
                        .show()
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

                R.id.add_prenotazione -> {
                    startActivity(Intent(this, PrenotazioneActivity::class.java))
                }

                R.id.prenotazioni -> {
                    impostaFragment(FragmentListaPrenotazioni())
                }
                else -> {}
            }
            true
        }

        viewModel.filePath.observe(this){filePath ->
            openFile(filePath)
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

    private fun openFile(filePath: String) {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(this, "${this.packageName}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, null)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        this.startActivity(intent)
    }
}
