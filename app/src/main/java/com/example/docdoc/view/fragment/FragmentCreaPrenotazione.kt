package com.example.docdoc.view.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.docdoc.databinding.FragmentInserisciPrenotazioneBinding
import com.example.docdoc.util.InputValidator
import com.example.docdoc.util.PrenotazioniUtil.Companion.calcolaSlotDisponibili
import com.example.docdoc.viewmodel.PrenotazioneViewModel
import java.util.Calendar


class FragmentCreaPrenotazione: Fragment() {
    private lateinit var binding: FragmentInserisciPrenotazioneBinding
    private val viewModel: PrenotazioneViewModel by viewModels({ requireActivity() })
    private val inputValidator = InputValidator()
    private val EMPTY_FIELD_ERROR = "Compila il campo"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInserisciPrenotazioneBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        setViewListeners()

        return binding.root
    }

    private fun setViewListeners() {


        binding.buttonIndietro.setOnClickListener {
            requireActivity().finish()
        }

        binding.buttonContinua.setOnClickListener {
            aggiungiPrenotazione()
        }

        // on below line we are adding
        // click listener for our edit text.
        binding.buttonEditData.setOnClickListener{
            showCalendarDialog()
        }

        binding.buttonEditOra.setOnClickListener {
            showOrariDialog()
        }


    }

    private fun showCalendarDialog() {
        // istanza di calendar
        val c = Calendar.getInstance()

        // vengono ottenuti anno, mese e giorno
        val anno = c.get(Calendar.YEAR)
        val mese = c.get(Calendar.MONTH)
        val giorno = c.get(Calendar.DAY_OF_MONTH)

        // viene creato il Dialog di scelta del giorno
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                // viene settata la data del button
                val data = (year.toString() + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth))
                binding.buttonEditData.setText(data)
                binding.buttonEditOra.setText("")
                viewModel.getPrenotazioniPerGiorno(giorno = binding.buttonEditData.text.toString())
            },
            // vengono passati anno, mese e giorno al Dialog
            anno,
            mese,
            giorno
        )
        // Imposta la data minima selezionabile a quella attuale
        datePickerDialog.datePicker.minDate = c.timeInMillis
        // viene visualizzato il dialog
        datePickerDialog.show()
    }

    private fun showOrariDialog(){
        val myList: ArrayList<String> = arrayListOf()

        if (viewModel.listaPrenotazioni.value != null) {
            for (item in calcolaSlotDisponibili(viewModel.listaPrenotazioni.value!!, binding.buttonEditData.text.toString())) {
                myList.add(item.orario!!)
            }
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Scegli un orario")

        builder.setItems(myList.toTypedArray()) { dialog, index ->
            binding.buttonEditOra.setText(myList[index])
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun aggiungiPrenotazione(){
        if(inputValidator.isFieldEmpty(binding.editNome.text.toString())){
            binding.editNome.error = EMPTY_FIELD_ERROR
        }
        else if(inputValidator.isFieldEmpty(binding.editCognome.text.toString())){
            binding.editCognome.error = EMPTY_FIELD_ERROR
        }
        else if(inputValidator.isFieldEmpty(binding.buttonEditData.text.toString())){
            binding.buttonEditData.error = EMPTY_FIELD_ERROR
        }
        else if(inputValidator.isFieldEmpty(binding.editNome.text.toString())){
            binding.editNome.error = EMPTY_FIELD_ERROR
        }
        else{
            viewModel.addPrenotazioneData()
        }
    }
}