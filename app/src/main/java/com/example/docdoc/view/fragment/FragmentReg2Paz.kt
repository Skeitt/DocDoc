package com.example.docdoc
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.adapter.DoctorListAdapter
import com.example.docdoc.databinding.FragmentReg2PazBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.util.InputValidator
import com.example.docdoc.viewmodel.FormViewModel

class FragmentReg2Paz : Fragment() {

    private lateinit var binding: FragmentReg2PazBinding
    /** il viewmodel appartiene all'activity in cui è contenuto il fragment ed
    è condiviso tra tutti i fragment */
    private val viewModel: FormViewModel by viewModels({ requireActivity() })
    private val inputValidator = InputValidator()

    // RecyclerView + SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchList: ArrayList<Utente>
    private lateinit var doctorListAdapter: DoctorListAdapter

    private val ADDRESS_ERROR = "Formato dell'indirizzo non valido"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReg2PazBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // viewModel
        binding.viewModel = viewModel

        // viene aggiornata la lista di medici
        viewModel.updateDoctorsList()

        // recyclerView
        recyclerView = binding.listaMedici
        searchView = binding.searchMedico
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // creo il dataset di medici
        searchList = arrayListOf<Utente>()

        // imposto l'adapter e lo lego al dataset
        doctorListAdapter = DoctorListAdapter(searchList)
        recyclerView.adapter = doctorListAdapter

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
                    viewModel.medici.value?.forEach {
                        if (it.nome!!.lowercase()
                                .contains(searchText) ||
                            it.cognome!!.lowercase()
                                .contains(searchText)
                        ) {
                            searchList.add(it)
                        }
                    }
                    doctorListAdapter.notifyDataSetChanged()
                } else {
                    searchList.clear()
                    searchList.addAll(viewModel.medici.value!!)
                    doctorListAdapter.notifyDataSetChanged()
                }
                return false
            }
        })

        doctorListAdapter.onItemClick = {medico ->
            val text = medico.nome + " " + medico.cognome
            searchView.setQuery(text, false)
            viewModel.setUidMedico(medico.uid!!)
        }

        // Quando la lista dei medici nel viewmodel varia, l'osservatore viene notificato e
        // cambia la lista degi medici
        viewModel.medici.observe(viewLifecycleOwner) { doctorsList ->
            doctorsList?.let {
                searchList.addAll(doctorsList)
            }
            doctorListAdapter.notifyDataSetChanged()
        }

        // registra il medico
        binding.buttonContinua.setOnClickListener(signUp())
        // torna al fragment precedente
        binding.buttonIndietro.setOnClickListener(goToPreviusFragment())

        binding.editRes.addTextChangedListener {
            viewModel.setIndirizzo(it.toString())
        }

        return binding.root
    }

    private fun signUp(): View.OnClickListener? {
        return View.OnClickListener {
            if(!inputValidator.isValidIndirizzo(binding.editRes.text.toString()))
            {
                binding.editRes.error = ADDRESS_ERROR
            }
            else{
                viewModel.pushUserData()
            }
        }
    }

    private fun goToPreviusFragment(): View.OnClickListener? {
        return View.OnClickListener {
            findNavController().popBackStack()
        }
    }
}