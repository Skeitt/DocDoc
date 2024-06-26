package com.example.docdoc.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityPrenotazioneBinding
import com.example.docdoc.uistate.PrenotazioneUiState
import com.example.docdoc.view.fragment.FragmentCreaPrenotazione
import com.example.docdoc.view.fragment.FragmentVisualizzaPrenotazione
import com.example.docdoc.viewmodel.PrenotazioneViewModel
import kotlinx.coroutines.launch

class PrenotazioneActivity : AppCompatActivity(){
    private lateinit var binding : ActivityPrenotazioneBinding
    private val viewModel : PrenotazioneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrenotazioneBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /** prenotazione prende il valore dello uid della prenotazione da visualizzare,
         * altrimenti significa che si vuole creare una nuova prenotazione
         */
        val pid: String? = intent.getStringExtra("pid")
        val dataEOra: String? = intent.getStringExtra("dataEOra")
        if(pid != null){
            viewModel.getPrenotazione(pid)
        }
        else if (dataEOra != null){
            viewModel.setOrarioOdierno(dataEOra.split("_")[0],dataEOra.split("_")[1])
            viewModel.setStato(PrenotazioneUiState.creating())
        }
        else
        {
            viewModel.setStato(PrenotazioneUiState.creating())
        }

        lifecycleScope.launch {
            viewModel.prenotazioneUiState.collect { stato ->
                Log.d("STATO", stato.toString())
                if(stato.isView){
                    impostaFragment(FragmentVisualizzaPrenotazione())
                }
                if (stato.isEdit){
                    impostaFragment(FragmentCreaPrenotazione())
                }
                if (stato.isCreating){
                    impostaFragment(FragmentCreaPrenotazione())
                }
                // se c'è un errore o la prenotazione è stata eliminata si chiude l'activity
                if(stato.isError || stato == PrenotazioneUiState())
                {
                    finish()
                }
            }
        }

        // TODO: Osservatore della prenotazione
    }

    private fun impostaFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.prenotazione_container, fragment)
            .commit()
    }
}