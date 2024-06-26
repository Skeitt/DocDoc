package com.example.docdoc.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.docdoc.databinding.FragmentInserisciEventoBinding
import com.example.docdoc.viewmodel.EventoViewModel
import java.util.Calendar

class FragmentInserisciEvento : Fragment() {

    private lateinit var binding: FragmentInserisciEventoBinding
    private val viewModel: EventoViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInserisciEventoBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editMotivo.addTextChangedListener {
            viewModel.setMotivo(it.toString())
        }
        binding.editData.addTextChangedListener{
            viewModel.setData(it.toString())
        }
        binding.editDescrizione.addTextChangedListener {
            viewModel.setDescrizione(it.toString())
        }

        binding.buttonEditData.setOnClickListener {
            showDatePickerDialog()
        }

        //TODO: manca tutta la parte della recyclerView per i file

        return binding.root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.editData.text = selectedDate
        }, year, month, day).show()
    }
}