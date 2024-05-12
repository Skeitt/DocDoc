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
import com.example.docdoc.databinding.FragmentReg1Binding
import com.example.docdoc.util.InputValidator
import com.example.docdoc.viewmodel.SignUpViewModel

class FragmentReg1 : Fragment() {

    private lateinit var binding: FragmentReg1Binding
    private val viewModel: SignUpViewModel by viewModels({ requireActivity() })
    private val inputValidator = InputValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReg1Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // viewModel
        binding.viewModel = viewModel

        //  Navigazione verso il prossimo fragment
        binding.buttonContinua.setOnClickListener(onContinua())
        //  Ritorno al Login
        binding.buttonIndietro.setOnClickListener(goToLogin())

        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editEmail.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }
        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editPw.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }

        return binding.root
    }


    private fun onContinua(): View.OnClickListener? {
        return View.OnClickListener {
            if (!inputValidator.isValidEmail(binding.editEmail.text.toString())) {
                Toast.makeText(context, "Formato email non valido", Toast.LENGTH_SHORT).show()
            } else if (!inputValidator.isValidPassword(binding.editPw.text.toString())) {
                Toast.makeText(context, "Formato password non valido", Toast.LENGTH_SHORT).show()
            } else if (binding.editPw.text.toString() != binding.confPw.text.toString()) {
                Toast.makeText(context, "Le password non corrispondono", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_fragment1_to_fragment2)
            }
        }
    }

    private fun goToLogin(): View.OnClickListener? {
        // si torna all'activity precedente ovvero il Login
        return View.OnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
