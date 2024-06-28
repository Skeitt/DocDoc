package com.example.docdoc.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.docdoc.adapter.UserListAdapter
import com.example.docdoc.databinding.FragmentProfiloPazienteBinding
import com.example.docdoc.model.Evento
import com.example.docdoc.model.Utente
import com.example.docdoc.view.activity.EventoActivity
import com.example.docdoc.view.activity.ModificaMalattieFarmaciActivity
import com.example.docdoc.view.activity.ModificaProfiloActivity
import com.example.docdoc.view.adapter.EventListAdapter
import com.example.docdoc.viewmodel.ModificaMalattieFarmaciViewModel
import com.example.docdoc.viewmodel.UtenteViewModel
import java.io.File

class FragmentProfiloPaziente : Fragment() {

    private lateinit var binding: FragmentProfiloPazienteBinding
    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })
    private val viewModelMalattieFarmaci: ModificaMalattieFarmaciViewModel by viewModels({ requireActivity() })

    // RecyclerView di eventi
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<Evento>
    private lateinit var eventListAdapter: EventListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfiloPazienteBinding.inflate(inflater, container, false)

        initView()
        setObserver()


        // recyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        // creo il dataset di eventi
        eventList = arrayListOf<Evento>()

        // imposto l'adapter e lo lego al dataset
        eventListAdapter = EventListAdapter(eventList)
        recyclerView.adapter = eventListAdapter

        setRvOnClick()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //quando viene riaperto il FragmentProfiloPaziente, se sono un medico riaggiorno
        // il contenuto del viewModelMalattieFarmaci.malattie e viewModelMalattieFarmaci.farmaci
        if (viewModel.currentUser.value!!.medico!!){
            viewModelMalattieFarmaci.fetchMalattieFarmaciPaziente(viewModel.user.value!!.uid!!)
        }
    }

    private fun goToEditProfile() : View.OnClickListener?{
        return View.OnClickListener {
            startActivity(Intent(activity, ModificaProfiloActivity::class.java))
        }
    }

    private fun goToEditCartellaClinica(uidPaziente: String): View.OnClickListener? {
        val intent: Intent = Intent(requireActivity() ,ModificaMalattieFarmaciActivity::class.java)
        //passo l'id del paziente all'activity che vado ad istanziare
        intent.putExtra("uid",uidPaziente)
        return View.OnClickListener {
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        viewModel.getListaEventi()
        if (viewModel.currentUser.value!!.medico!!){
            //rendo visibile il pulsante per la modifica della cartella clinica
            binding.buttonEditCartellaClinica.visibility = View.VISIBLE

            //visualizzo nelle textView i dati del paziente che sono presenti all'interno di viewModel.user
            viewModel.user.observe(viewLifecycleOwner, Observer { user ->
                // Popola le TextView con i dati del paziente
                binding.tvDatiPersonali.setText("Nome: " + user.nome + "\nCognome: " + user.cognome +
                        "\nNumero di Telefono: " + user.numDiTelefono + "\nCodice Fiscale: " +
                        user.codiceFiscale + "\nData di Nascita: " + user.dataDiNascita +
                        "\nIndirizzo: " + user.indirizzo)
            })

            binding.buttonEditCartellaClinica.setOnClickListener(goToEditCartellaClinica(viewModel.user.value!!.uid!!))

        }else{
            //rendo visibile il pulsante per la modifica dei dati
            binding.buttonModificaProfilo.visibility = View.VISIBLE
            binding.buttonLogout.visibility = View.VISIBLE

            //visualizzo nelle textView i dati del paziente che sono presenti all'interno di viewModel.currentUser
            viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
                // Popola le TextView con i dati del paziente
                binding.tvDatiPersonali.setText("Nome: " + user.nome + "\nCognome: " + user.cognome +
                        "\nNumero di Telefono: " + user.numDiTelefono + "\nCodice Fiscale: " +
                        user.codiceFiscale + "\nData di Nascita: " + user.dataDiNascita +
                        "\nIndirizzo: " + user.indirizzo)
            })

            binding.buttonModificaProfilo.setOnClickListener(goToEditProfile())
            binding.buttonLogout.setOnClickListener{viewModel.logout()}
        }

        viewModel.filePath.observe(viewLifecycleOwner){filePath ->
            openFile(filePath)
        }

        //sia medico che paziente possono aggiungere un evento
        //nel momento in cui viene cliccato il button viene startata l'activity: EventoActivity
        //e viene caricato il fragment: FragmentInserisciEvento all'interno  dell'activity
        binding.buttonAggEvento.setOnClickListener {
            startEvento()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObserver(){
        //visualizzo nella textView le malattie e i farmaci del paziente che sono presenti all'interno di viewModel.farmaci e viewModel.malattie
        viewModelMalattieFarmaci.malattie.observe(viewLifecycleOwner, Observer { malattie ->
            // Popola le TextView con i dati del paziente
            viewModelMalattieFarmaci.farmaci.observe(viewLifecycleOwner, Observer{farmaci ->
                //prima di popolare la textView delle malattie e dei farmaci verifico che non siano vuoti
                binding.tvMalEFarm.setText("Malattie: " + if (malattie?.isNotEmpty() == true) { malattie.joinToString(", ") }else{" Nessuna "} +
                        "\nFarmaci: " + if (farmaci?.isNotEmpty() == true) { farmaci.joinToString(", ") }else{" Nessuno "} )
            })
        })

        // Quando la lista degli eventi nel viewmodel varia, l'osservatore viene notificato
        viewModel.listaEventi.observe(viewLifecycleOwner) { listaEventi ->
            listaEventi?.let {
                eventList.clear()
                eventList.addAll(listaEventi.sortedByDescending { it.data })
            }
            eventListAdapter.notifyDataSetChanged()
        }
    }

    private fun startEvento(){
        val intent = Intent(activity, EventoActivity::class.java)
        intent.putExtra("FRAGMENT_TYPE", "INSERT")
        //se è un medico che vuole aggiungere l'evento
        if (viewModel.currentUser.value!!.medico!!){
            intent.putExtra("UID_PAZIENTE",viewModel.user.value!!.uid)
        }else{ //se è un paziente che vuole aggiungere l'evento
            intent.putExtra("UID_PAZIENTE",viewModel.currentUser.value!!.uid)
        }
        startActivity(intent)
    }

    private fun openFile(filePath: String) {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, null)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        this.startActivity(intent)
    }

    private fun setRvOnClick(){
        eventListAdapter.onFileItemClick = { evento, filename ->
            val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val localFile = File.createTempFile(filename.split(".")[0], "."+filename.split(".")[1], downloadFolder)
            viewModel.getFile(evento.eid!!, filename, localFile)
        }

        eventListAdapter.onItemClick = {evento ->
            val intent = Intent(activity, EventoActivity::class.java)
            intent.putExtra("FRAGMENT_TYPE", evento.eid)
            startActivity(intent)
        }
    }
}