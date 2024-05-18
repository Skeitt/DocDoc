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
import com.example.docdoc.databinding.FragmentReg2Binding
import com.example.docdoc.util.InputValidator
import com.example.docdoc.viewmodel.SignUpViewModel

class FragmentReg2 : Fragment() {

    private lateinit var binding: FragmentReg2Binding
    /** il viewmodel appartiene all'activity in cui è contenuto il fragment ed
    è condiviso tra tutti i fragment */
    private val viewModel: SignUpViewModel by viewModels({ requireActivity() })
    private val inputValidator = InputValidator()

    private val EMPTY_FIELD_ERROR = "Compila il campo"
    private val TEL_ERROR = "Formato del numero di telefono non valido"
    private val CF_ERROR = "Formato del codice fiscale non valido"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReg2Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // viewModel
        binding.viewModel = viewModel

        //  Navigazione verso il prossimo fragment
        binding.buttonContinua.setOnClickListener(goToNextFragment())
        //  Ritorno al fragment precedente
        binding.buttonIndietro.setOnClickListener(goToPreviusFragment())

        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editNome.addTextChangedListener {
            viewModel.setNome(it.toString())
        }
        binding.editCognome.addTextChangedListener {
            viewModel.setCognome(it.toString())
        }
        binding.editCF.addTextChangedListener {
            viewModel.setCodiceFiscale(it.toString())
        }
        binding.editTel.addTextChangedListener {
            viewModel.setNumDiTelefono(it.toString())
        }

        return binding.root
    }

    private fun goToPreviusFragment(): View.OnClickListener? {
        return View.OnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun goToNextFragment(): View.OnClickListener? {
        return View.OnClickListener {
            if(inputValidator.isFieldEmpty(binding.editNome.text.toString()))
            {
                binding.editNome.error = EMPTY_FIELD_ERROR
            }
            else if(inputValidator.isFieldEmpty(binding.editCognome.text.toString()))
            {
                binding.editCognome.error = EMPTY_FIELD_ERROR
            }
            if (!inputValidator.isValidNumeroDiTelefono(binding.editTel.text.toString()))
            {
                binding.editTel.error = TEL_ERROR
            }
            else if (!inputValidator.isValidCodiceFiscale(binding.editCF.text.toString()))
            {
                binding.editCF.error = CF_ERROR
            }
            else if (binding.radioPaziente.isChecked) {
                findNavController().navigate(R.id.action_fragment2_to_fragment2Paz)
            }
            else {
                findNavController().navigate(R.id.action_fragment2_to_fragment2Med)
            }
        }
    }
}