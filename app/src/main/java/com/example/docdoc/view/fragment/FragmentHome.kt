package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.R
import com.example.docdoc.adapter.UserListAdapter
import com.example.docdoc.databinding.FragmentHomeBinding
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.model.Utente
import com.example.docdoc.util.PrenotazioniUtil.Companion.calcolaSlotDisponibili
import com.example.docdoc.view.adapter.BookingListAdapter
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentHome : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookingRecyclerView: RecyclerView
    private lateinit var bookingList: ArrayList<Prenotazione>
    private lateinit var bookingListAdapter: BookingListAdapter

    private lateinit var searchView: SearchView
    private lateinit var pazientiRecyclerView: RecyclerView
    private lateinit var searchList: ArrayList<Utente>
    private lateinit var pazienteListAdapter: UserListAdapter

    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val isMedico = viewModel.currentUser.value?.medico!!

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // vengono impostate le varie View in base al ruolo dell'utente
        initView(isMedico)

        // recyclerView per la lista delle prenotazioni
        bookingRecyclerView = binding.listaSlotOrari
        bookingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingRecyclerView.setHasFixedSize(true)

        // recyclerView per la lista dei pazienti
        pazientiRecyclerView = binding.listaPazienti
        searchView = binding.searchPaziente
        pazientiRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        pazientiRecyclerView.setHasFixedSize(true)

        /** PRENOTAZIONI */
        // creo il dataset di prenotazioni
        bookingList = arrayListOf<Prenotazione>()

        // imposto l'adapter e lo lego al dataset delle prenotazioni
        bookingListAdapter = BookingListAdapter(
            bookingList = bookingList,
            isMedico = isMedico
        )
        bookingRecyclerView.adapter = bookingListAdapter

        viewModel.listaPrenotazioni.observe(viewLifecycleOwner){listaPrenotazioni ->
            onListChange(listaPrenotazioni, isMedico)
        }
        setOnViewChange(isMedico)

        /** RICERCA DEI PAZIENTI */
        searchList = arrayListOf<Utente>()

        pazienteListAdapter = UserListAdapter(
            searchList
        )
        pazientiRecyclerView.adapter = pazienteListAdapter

        setSearchingAlgo()
        setOsservatorePazienti()

        return binding.root
    }

    private fun onListChange(listaPrenotazioni: ArrayList<Prenotazione>, isMedico: Boolean)
    {
        listaPrenotazioni?.let {
            bookingList.clear()
            if(isMedico){
                bookingList.addAll(listaPrenotazioni)
            }
            else{
                bookingList.addAll(calcolaSlotDisponibili(listaPrenotazioni))
            }
        }
        bookingListAdapter.notifyDataSetChanged()
    }

    private fun setOnViewChange(isMedico: Boolean)
    {
        bookingListAdapter.onItemClick = {prenotazione ->
            if(isMedico)
            {
                // TODO: Apri la pagina nuova prenotazione con inserito l'orario
            }
            else
            {
                // TODO: Apri la pagina visualizza prenotazione
            }
        }
        binding.buttonProfiloMedico.setOnClickListener(goToDoctorProfile(viewModel.currentUser.value?.uidMedico!!))

        pazienteListAdapter.onItemClick = {paziente ->
            val text = paziente.nome + " " + paziente.cognome
            searchView.setQuery(text, false)
            // TODO: Apri visualizza paziente
        }
    }

    private fun initView(isMedico: Boolean)
    {
        binding.recyclerViewTitle.text = if (isMedico) "Prenotazioni Odierne" else "Prenota"
        binding.buttonProfiloMedico.visibility = if(isMedico) View.INVISIBLE else View.VISIBLE
        binding.searchPaziente.visibility = if(isMedico) View.VISIBLE else View.INVISIBLE
        binding.listaPazienti.visibility = if(isMedico) View.VISIBLE else View.GONE
    }

    private fun goToDoctorProfile(uidMedico: String): View.OnClickListener?{
        return View.OnClickListener {
            viewModel.getUser(uidMedico)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentProfiloMedico())
                .commit()
        }
    }

    private fun setSearchingAlgo(){
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList.clear()
                val searchText = newText!!.lowercase()
                if (searchText.isNotEmpty()) {
                    viewModel.pazienti.value?.forEach {
                        if (it.nome!!.lowercase()
                                .contains(searchText) ||
                            it.cognome!!.lowercase()
                                .contains(searchText)
                        ) {
                            searchList.add(it)
                        }
                    }
                    pazienteListAdapter.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(viewModel.pazienti.value!!)
                    pazienteListAdapter.notifyDataSetChanged()
                }
                return false
            }
        })
    }

    private fun setOsservatorePazienti()
    {
        // Quando la lista dei medici nel viewmodel varia, l'osservatore viene notificato e
        // cambia la lista degi medici
        viewModel.pazienti.observe(viewLifecycleOwner) { listaPazienti ->
            listaPazienti?.let {
                searchList.addAll(listaPazienti)
            }
            pazienteListAdapter.notifyDataSetChanged()
        }
    }
}