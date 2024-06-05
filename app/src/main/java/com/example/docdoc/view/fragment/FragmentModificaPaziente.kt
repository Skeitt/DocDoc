package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.docdoc.databinding.FragmentModificaPazienteBinding
import com.example.docdoc.viewmodel.ModificaProfiloViewModel
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentModificaPaziente: Fragment() {

    private lateinit var binding: FragmentModificaPazienteBinding
    private val viewModel: ModificaProfiloViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModificaPazienteBinding.inflate(inflater, container, false)

        viewModel.editUser.observe(viewLifecycleOwner, Observer { user ->
            // Popola le TextView con i dati dell'utente che si vogliono modificare
            binding.editNome.setText(user.nome)
            binding.editCognome.setText(user.cognome)
            binding.editCF.setText(user.codiceFiscale)
            binding.editTel.setText(user.numDiTelefono)
            binding.editRes.setText(user.indirizzo)
        })

        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editNome.addTextChangedListener {
            viewModel.setNome(it.toString())
        }
        binding.editCognome.addTextChangedListener {
            viewModel.setCognome(it.toString())
        }
        binding.editCF.addTextChangedListener {
            viewModel.setCF(it.toString())
        }
        binding.editTel.addTextChangedListener {
            viewModel.setTelefono(it.toString())
        }
        binding.editRes.addTextChangedListener {
            viewModel.setIndirizzo(it.toString())
        }

        return binding.root
    }

}