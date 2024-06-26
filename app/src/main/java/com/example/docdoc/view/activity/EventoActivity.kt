package com.example.docdoc.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityEventoBinding
import com.example.docdoc.view.fragment.FragmentInserisciEvento
import com.example.docdoc.view.fragment.FragmentModificaEvento
import com.example.docdoc.viewmodel.EventoViewModel
import kotlinx.coroutines.launch

class EventoActivity: AppCompatActivity() {

    //viewModel
    private val viewModel: EventoViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityEventoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragment_type: String = intent.getStringExtra("FRAGMENT_TYPE")!!

        //in base alla stringa passata all'interno dell'Intent carico il Fragment
        val fragment: Fragment = when (fragment_type) {
            "INSERT" -> FragmentInserisciEvento()
            "EDIT" -> FragmentModificaEvento()
            else -> FragmentInserisciEvento() // caso: Default (non lo avremo mai)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()


        if (fragment_type == "INSERT"){
            binding.btnBlue.text = "Indietro"
            binding.btnBlue.setOnClickListener(goBack())
            binding.btnGreen.text = "Aggiungi"
            binding.btnGreen.setOnClickListener(inserisciDatiEvento())
        }else{
            binding.btnBlue.text = "Elimina"
            binding.btnBlue.setOnClickListener(deleteEvento())
            binding.btnGreen.text = "Continua"
            binding.btnGreen.setOnClickListener(salvaDatiEvento())
        }

        lifecycleScope.launch {
            viewModel.eventoUiState.collect{
                if (it.isCreated){
                    //torno al fragment del profilo
                    finish()
                }
                if (it.isModified){
                    //torno al fragment del profilo
                    finish()
                }
                if (it.isEliminated){
                    //torno al fragment del profilo
                    finish()
                }
                if (it.isError){
                    //visualizzo un messaggio di errore
                    Toast.makeText(this@EventoActivity, "Errore", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    private fun goBack() : View.OnClickListener{
        return View.OnClickListener {
            //torno al fragment del profilo
            finish()
        }
    }

    private fun inserisciDatiEvento() : View.OnClickListener{
        return View.OnClickListener {
            val uidPaziente: String = intent.getStringExtra("UID_PAZIENTE")!!
            viewModel.setEventID()
            viewModel.setUidPaziente(uidPaziente)
            viewModel.setEventData()
        }
    }

    private fun salvaDatiEvento() : View.OnClickListener{
        return View.OnClickListener {
            //viewModel.updateEventData()
        }
    }

    private fun deleteEvento() : View.OnClickListener{
        return View.OnClickListener {
            //viewModel.deleteEventData()
        }
    }
}