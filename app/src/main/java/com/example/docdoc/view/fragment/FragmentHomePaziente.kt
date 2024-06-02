package com.example.docdoc.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.databinding.FragmentHomePazienteBinding
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.util.PrenotazioniUtil.Companion.creaListaPrenotazioni
import com.example.docdoc.view.adapter.BookingListAdapter
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentHomePaziente : Fragment() {

    private lateinit var binding: FragmentHomePazienteBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingList: ArrayList<Prenotazione>
    private lateinit var bookingListAdapter: BookingListAdapter

    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePazienteBinding.inflate(inflater, container, false)

        // recyclerView
        recyclerView = binding.listaSlotOrari
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)


        // Questa operazione va fatta solo una volta ovvero all'istanziazione della mainactivity e basta
        // l'aggiornamento del dataset va fatto lato view con l'observe
        // e quando viene aggiornato il database
        // viewModel.getPrenotazioniPerGiorno(giorno = "2024-05-31")

        // creo il dataset di prenotazioni
        bookingList = arrayListOf<Prenotazione>()

        // imposto l'adapter e lo lego al dataset
        bookingListAdapter = BookingListAdapter(
            bookingList = bookingList,
            isMedico = viewModel.currentUser.value?.medico!!
        )
        recyclerView.adapter = bookingListAdapter

        bookingListAdapter.onItemClick = {prenotazione ->
            Log.d("PRENOTAZIONE", prenotazione.nomePaziente!!)
        }

        viewModel.listaPrenotazioni.observe(viewLifecycleOwner){listaPrenotazioni ->
            listaPrenotazioni?.let {
                bookingList.clear()
                if(viewModel.currentUser.value?.medico!!){
                    bookingList.addAll(listaPrenotazioni)
                }
                else{
                    bookingList.addAll(calcolaSlotDisponibili(listaPrenotazioni))
                }
            }
            bookingListAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

    private fun calcolaSlotDisponibili(lista : ArrayList<Prenotazione>) : ArrayList<Prenotazione>
    {
        var slotDisponibili: ArrayList<Prenotazione> = creaListaPrenotazioni()
        for (prenotazione in lista)
        {
            slotDisponibili.removeIf {
                it.orario == prenotazione.orario
            }
        }

        return slotDisponibili
    }
}