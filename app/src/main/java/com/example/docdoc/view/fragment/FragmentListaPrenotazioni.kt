package com.example.docdoc.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.databinding.FragmentListaPrenotazioniBinding
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.view.activity.PrenotazioneActivity
import com.example.docdoc.view.adapter.BookingListAdapter
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentListaPrenotazioni : Fragment() {
    private lateinit var binding: FragmentListaPrenotazioniBinding
    private lateinit var bookingRecyclerView: RecyclerView
    private lateinit var bookingList: ArrayList<Prenotazione>
    private lateinit var bookingListAdapter: BookingListAdapter

    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val isMedico = viewModel.currentUser.value?.medico!!

        binding = FragmentListaPrenotazioniBinding.inflate(inflater, container, false)

        // recyclerView per la lista delle prenotazioni
        bookingRecyclerView = binding.recyclerViewPrenotazioni
        bookingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingRecyclerView.setHasFixedSize(true)

        /** PRENOTAZIONI */
        // creo il dataset di prenotazioni
        bookingList = arrayListOf<Prenotazione>()

        // imposto l'adapter e lo lego al dataset delle prenotazioni
        bookingListAdapter = BookingListAdapter(
            bookingList = bookingList,
            isCompressed = false
        )
        bookingRecyclerView.adapter = bookingListAdapter

        setOnViewChange()
        setObserver(isMedico)

        return binding.root
    }

    private fun setOnViewChange()
    {
        bookingListAdapter.onItemClick = {prenotazione ->
            val intent: Intent = Intent(
                requireActivity(),
                PrenotazioneActivity::class.java
            )
            intent.putExtra(
                "pid",
                prenotazione.pid
            ) // passa l'ID della prenotazione

            startActivity(intent)
        }
    }

    private fun setObserver(isMedico: Boolean)
    {
        // viene osservata la lista delle prenotazioni presente nel viemodel
        viewModel.listaPrenotazioniUtente.observe(viewLifecycleOwner){ listaPrenotazioni ->
            onListChange(listaPrenotazioni)
        }

        // quando viene settato l'utente va effettuata la scansione delle prenotazioni
        viewModel.currentUser.observe(viewLifecycleOwner)
        {
            viewModel.getPrenotazioniUtente(isMedico)
        }
    }

    private fun onListChange(listaPrenotazioni: ArrayList<Prenotazione>)
    {
        listaPrenotazioni.let {
            bookingList.clear()
            bookingList.addAll(listaPrenotazioni)
        }
        bookingListAdapter.notifyDataSetChanged()
    }
}
