package com.example.docdoc.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.docdoc.databinding.FragmentProfiloMedicoBinding
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentProfiloMedico : Fragment() {

    private lateinit var binding: FragmentProfiloMedicoBinding
    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfiloMedicoBinding.inflate(inflater, container, false)

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                // Popola le TextView con i dati del dottore
                binding.tvDatiPersonali.text = "Nome: " + it.nome + "\nCognome: " + it.cognome +
                        "\nNumero di Telefono: " + it.numDiTelefono + "\nCodice Fiscale: " +
                        it.codiceFiscale + "\nData di Nascita: " + it.dataDiNascita

                binding.indAmbulatorio.text = it.indirizzo
            }
        })

        return binding.root
    }
}