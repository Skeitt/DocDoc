package com.example.docdoc.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.docdoc.databinding.FragmentProfiloPazienteBinding
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

        return binding.root
    }


    private fun goToEditProfile() : View.OnClickListener?{
        return View.OnClickListener {
            startActivity(Intent(activity, ModificaProfiloActivity::class.java))
        }
    }

    private fun goToEditCartellaClinica(uidPaziente: String): View.OnClickListener? {
        viewModelMalattieFarmaci.fetchMalattieFarmaciPaziente(uidPaziente)
        return View.OnClickListener {
            startActivity(Intent(activity, ModificaMalattieFarmaciActivity::class.java))
        }
    }
}