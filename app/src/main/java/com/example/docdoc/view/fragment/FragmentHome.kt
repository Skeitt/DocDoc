package com.example.docdoc.view.fragment

import android.content.Intent
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
import com.example.docdoc.util.DateUtil.Companion.getCurrentDate
import com.example.docdoc.util.PrenotazioniUtil.Companion.calcolaSlotDisponibili
import com.example.docdoc.view.activity.PrenotazioneActivity
import com.example.docdoc.view.adapter.BookingListAdapter
import com.example.docdoc.viewmodel.ModificaMalattieFarmaciViewModel
import com.example.docdoc.viewmodel.UtenteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
    private val viewModelMalattieFarmaci: ModificaMalattieFarmaciViewModel by viewModels({ requireActivity() })
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
            bookingList = bookingList
        )
        bookingRecyclerView.adapter = bookingListAdapter

        /** RICERCA DEI PAZIENTI */
        searchList = arrayListOf<Utente>()

        pazienteListAdapter = UserListAdapter(
            searchList
        )
        pazientiRecyclerView.adapter = pazienteListAdapter

        setOnViewChange(isMedico)
        setSearchingAlgo()
        setObserver(isMedico)

        return binding.root
    }

    private fun onListChange(listaPrenotazioni: ArrayList<Prenotazione>, isMedico: Boolean)
    {
        listaPrenotazioni.let {
            bookingList.clear()
            if(isMedico){
                bookingList.addAll(listaPrenotazioni)
            }
            else{
                // data di oggi
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = Date()
                val todayString = dateFormat.format(today)
                bookingList.addAll(calcolaSlotDisponibili(listaPrenotazioni, todayString))
            }
        }
        bookingListAdapter.notifyDataSetChanged()
    }

    private fun setOnViewChange(isMedico: Boolean)
    {
        bookingListAdapter.onItemClick = {prenotazione ->
            if(isMedico)
            {
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
            else if(!isMedico)
            {
                val intent: Intent = Intent(
                    requireActivity(),
                    PrenotazioneActivity::class.java
                )
                val dataEOra = getCurrentDate() + "_" + prenotazione.orario
                intent.putExtra(
                    "dataEOra",
                    dataEOra,
                ) // passa l'orario della prenotazione

                startActivity(intent)
            }
        }

        if (!isMedico){
            binding.buttonProfiloMedico.setOnClickListener(goToDoctorProfile(viewModel.currentUser.value!!.uidMedico!!))
        }

        pazienteListAdapter.onItemClick = {paziente ->
            val text = paziente.nome + " " + paziente.cognome
            searchView.setQuery(text, false)

            viewModel.setUser(paziente)
            viewModelMalattieFarmaci.fetchMalattieFarmaciPaziente(paziente.uid!!)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentProfiloPaziente())
                .commit()
        }
    }

    private fun initView(isMedico: Boolean)
    {
        binding.recyclerViewTitle.text = if (isMedico) "Prenotazioni Odierne" else "Prenota per Oggi"
        binding.buttonProfiloMedico.visibility = if(isMedico) View.INVISIBLE else View.VISIBLE
        binding.searchPaziente.visibility = if(isMedico) View.VISIBLE else View.INVISIBLE
        binding.mieiPazienti.visibility = if(isMedico) View.VISIBLE else View.INVISIBLE
        binding.listaPazienti.visibility = if(isMedico) View.VISIBLE else View.GONE
        if (!isMedico){
            viewModelMalattieFarmaci.fetchMalattieFarmaciPaziente(viewModel.currentUser.value!!.uid!!)
        }
        if(isMedico){
            viewModel.getListaPazienti()
        }
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

    private fun setObserver( isMedico: Boolean)
    {
        // viene osservata la lista delle prenotazioni presente nel viemodel
        viewModel.listaPrenotazioniOdierne.observe(viewLifecycleOwner){ listaPrenotazioni ->
            onListChange(listaPrenotazioni, isMedico)
        }
        // Quando la lista dei pazienti nel viewmodel varia, l'osservatore viene notificato e
        // cambia la lista dei pazienti
        viewModel.pazienti.observe(viewLifecycleOwner) { listaPazienti ->
            listaPazienti?.let {
                searchList.clear()
                searchList.addAll(listaPazienti)
            }
            pazienteListAdapter.notifyDataSetChanged()
        }

        // quando viene settato l'utente va effettuata la scansione delle prenotazioni odierne
        viewModel.currentUser.observe(viewLifecycleOwner)
        {
            viewModel.getPrenotazioniPerGiorno(giorno = getCurrentDate())
        }
    }
}