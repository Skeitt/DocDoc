package com.example.docdoc.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.docdoc.databinding.FragmentProfiloPazienteBinding
import com.example.docdoc.view.activity.EventoActivity
import com.example.docdoc.view.activity.ModificaMalattieFarmaciActivity
import com.example.docdoc.view.activity.ModificaProfiloActivity
import com.example.docdoc.viewmodel.ModificaMalattieFarmaciViewModel
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentProfiloPaziente : Fragment() {

    private lateinit var binding: FragmentProfiloPazienteBinding
    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })
    private val viewModelMalattieFarmaci: ModificaMalattieFarmaciViewModel by viewModels({ requireActivity() })

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfiloPazienteBinding.inflate(inflater, container, false)

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

        //visualizzo nella textView le malattie e i farmaci del paziente che sono presenti all'interno di viewModel.farmaci e viewModel.malattie
        viewModelMalattieFarmaci.malattie.observe(viewLifecycleOwner, Observer { malattie ->
            // Popola le TextView con i dati del paziente
            viewModelMalattieFarmaci.farmaci.observe(viewLifecycleOwner, Observer{farmaci ->
                binding.tvMalEFarm.setText("Malattie: " + malattie?.joinToString(", ") + "\nFarmaci: " +
                        farmaci?.joinToString(", ") )
            })
        })

        //sia medico che paziente possono aggiungere un evento
        //nel momento in cui viene cliccato il button viene startata l'activity: EventoActivity
        //e viene caricato il fragment: FragmentInserisciEvento all'interno  dell'activity
        binding.buttonAggEvento.setOnClickListener {
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

        //TODO: per quanto riguarda la modifica di un Evento, mettere questo
//        binding.buttonEditEvento.setOnClickListener{
//            val intent = Intent(activity, EventoActivity::class.java)
//            intent.putExtra("FRAGMENT_TYPE", "EDIT")
//            startActivity(intent)
//        }

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
}