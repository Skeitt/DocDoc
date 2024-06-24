package com.example.docdoc.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.databinding.ActivityModificaMalattieFarmaciBinding
import com.example.docdoc.view.adapter.FarmaciAdapter
import com.example.docdoc.view.adapter.MalattieAdapter
import com.example.docdoc.viewmodel.ModificaMalattieFarmaciViewModel
import kotlinx.coroutines.launch

class ModificaMalattieFarmaciActivity: AppCompatActivity() {

    private val viewModel: ModificaMalattieFarmaciViewModel by viewModels()
    private lateinit var malattieAdapter: MalattieAdapter
    private lateinit var farmaciAdapter: FarmaciAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityModificaMalattieFarmaciBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //recupero l'id del paziente all'interno dell'intent
        val uidPaziente: String = intent.getStringExtra("uid")!!
        viewModel.setUserId(uidPaziente)
        viewModel.fetchMalattieFarmaciPaziente(uidPaziente)

        //recyclerView per Farmaci e Malattie
        val recyclerViewMalattie: RecyclerView = binding.recyclerViewMalattie
        val recyclerViewFarmaci: RecyclerView = binding.recyclerViewFarmaci

        //button per l'aggiunta dei farmaci e delle malattie
        val btnAddMalattia: Button = binding.buttonAggiungiMalattia
        val btnAddFarmaco: Button = binding.buttonAggiungiFarmaco

        recyclerViewMalattie.layoutManager = LinearLayoutManager(this)
        recyclerViewFarmaci.layoutManager = LinearLayoutManager(this)

        malattieAdapter = MalattieAdapter(mutableListOf()) { position ->
            viewModel.removeMalattia(position)
        }
        farmaciAdapter = FarmaciAdapter(mutableListOf()) { position ->
            viewModel.removeFarmaco(position)
        }

        recyclerViewMalattie.adapter = malattieAdapter
        recyclerViewFarmaci.adapter = farmaciAdapter

        viewModel.malattie.observe(this) { malattie ->
            malattieAdapter.updateData(malattie!!)
        }

        viewModel.farmaci.observe(this) { farmaci ->
            farmaciAdapter.updateData(farmaci!!)
        }

        btnAddMalattia.setOnClickListener {
            val newMalattia = binding.editMalattie.text
            viewModel.addMalattia(newMalattia.toString())
        }

        btnAddFarmaco.setOnClickListener {
            val newFarmaco = binding.editFarmaci.text
            viewModel.addFarmaco(newFarmaco.toString())
        }

        lifecycleScope.launch {
            viewModel.modificaUiState.collect{
                if(it.isModified){
                    //torno al fragment del profilo
                    finish()
                }
                if (it.isError){
                    //visualizzo un messaggio di errore
                    Toast.makeText(this@ModificaMalattieFarmaciActivity, "Errore", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.indietroButton.setOnClickListener(goBack())
        binding.salvaButton.setOnClickListener(salvaDati())
    }

    private fun goBack() : View.OnClickListener?{
        return View.OnClickListener {
            //torno al fragment del profilo del paziente
            finish()
        }
    }

    private fun salvaDati() : View.OnClickListener?{
        return View.OnClickListener {
            viewModel.updateCartellaClinica(viewModel.userId.value!!)
            viewModel.fetchMalattieFarmaciPaziente(viewModel.userId.value!!)
        }
    }
}