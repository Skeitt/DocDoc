package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.docdoc.databinding.FragmentVistaPrenotazioneBinding
import com.example.docdoc.uistate.PrenotazioneUiState
import com.example.docdoc.util.PrenotazioniUtil
import com.example.docdoc.viewmodel.PrenotazioneViewModel

class FragmentVisualizzaPrenotazione: Fragment() {
    private lateinit var binding: FragmentVistaPrenotazioneBinding
    private val viewModel: PrenotazioneViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVistaPrenotazioneBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        if(!PrenotazioniUtil.isDateMinoreDiOggi(viewModel.prenotazione.value?.data!!, viewModel.prenotazione.value?.orario!! ))
        {
            binding.buttonModificaPrenotazione.visibility = View.VISIBLE
            binding.buttonElimina.visibility = View.VISIBLE
        }

        setViewListeners()

        return binding.root
    }

    private fun setViewListeners(){
        binding.buttonIndietro.setOnClickListener{
            requireActivity().finish()
        }

        binding.buttonModificaPrenotazione.setOnClickListener{
            viewModel.setStato(PrenotazioneUiState.editing())
        }

        binding.buttonElimina.setOnClickListener {
            viewModel.deletePrenotazione(viewModel.prenotazione.value?.pid!!)
        }
    }
}