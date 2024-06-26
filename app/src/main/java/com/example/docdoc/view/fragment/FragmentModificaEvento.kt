package com.example.docdoc.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.docdoc.databinding.FragmentModificaEventoBinding
import com.example.docdoc.viewmodel.EventoViewModel
import java.util.Calendar

class FragmentModificaEvento : Fragment() {

    private lateinit var binding: FragmentModificaEventoBinding
    private val viewModel: EventoViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModificaEventoBinding.inflate(inflater, container, false)

        viewModel.editEvent.observe(viewLifecycleOwner, Observer { event ->
            // Popola le TextView con i dati dell'utente che si vogliono modificare
            binding.editMotivo.setText(event.motivo)
            binding.buttonEditData.setText(event.data)
            binding.editDescrizione.setText(event.descrizione)
        })

        //  Aggiorna il ViewModel quando cambia il testo negli EditText
        binding.editMotivo.addTextChangedListener {
            viewModel.setMotivoToEditEvent(it.toString())
        }
        binding.buttonEditData.addTextChangedListener {
            viewModel.setDataToEditEvent(it.toString())
        }
        binding.editDescrizione.addTextChangedListener {
            viewModel.setDescrizioneToEditEvent(it.toString())
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
            binding.buttonEditData.text = selectedDate
        }, year, month, day).show()
    }

}