package com.example.docdoc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.docdoc.databinding.FragmentReg2MedBinding
import com.example.docdoc.util.InputValidator
import com.example.docdoc.viewmodel.SignUpViewModel

class FragmentReg2Med : Fragment() {

    private lateinit var binding: FragmentReg2MedBinding
    private val viewModel: SignUpViewModel by viewModels({ requireActivity() })
    private val inputValidator = InputValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReg2MedBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // viewModel
        binding.viewModel = viewModel

        // registra il medico
        binding.buttonContinua.setOnClickListener(signUp())
        // torna al fragment precedente
        binding.buttonIndietro.setOnClickListener(goToPreviusFragment())

        binding.editAmbulatorio.addTextChangedListener {
            viewModel.setIndirizzo(it.toString())
        }

        return binding.root
    }

    private fun goToPreviusFragment() : View.OnClickListener
    {
        return View.OnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun signUp(): View.OnClickListener? {
        return View.OnClickListener {
            if(!inputValidator.isValidIndirizzo(binding.editAmbulatorio.text.toString())){
                Toast.makeText(context, "Formato dell'indirizzo non valido", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // TODO: implementare anche lo switch di schermata ecc...
                viewModel.signUp()
            }
        }
    }
}