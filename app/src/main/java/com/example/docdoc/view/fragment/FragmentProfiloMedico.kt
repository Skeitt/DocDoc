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
import com.example.docdoc.databinding.FragmentProfiloMedicoBinding
import com.example.docdoc.view.activity.ModificaProfiloActivity
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentProfiloMedico : Fragment() {

    private lateinit var binding: FragmentProfiloMedicoBinding
    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfiloMedicoBinding.inflate(inflater, container, false)

        binding.indAmbulatorio.setOnClickListener { redirectToMaps(binding.indAmbulatorio.text.toString()) }

        if (viewModel.currentUser.value!!.medico!!) {
            //rendo visibile il pulsante per la modifica dei dati
            binding.buttonModificaProfilo.visibility = View.VISIBLE
            binding.buttonLogout.visibility = View.VISIBLE

            //visualizzo nelle textView i dati del dottore che sono presenti all'interno di viewModel.currentUser
            viewModel.currentUser.observe(viewLifecycleOwner, Observer { user ->
                // Popola le TextView con i dati del dottore
                binding.tvDatiPersonali.setText("Nome: " + user.nome + "\nCognome: " + user.cognome +
                        "\nNumero di Telefono Ambulatorio: " + user.numDiTelefono + "\nCodice Fiscale: " +
                        user.codiceFiscale + "\nData di Nascita: " + user.dataDiNascita)
                binding.indAmbulatorio.setText(user.indirizzo)
            })

            binding.buttonModificaProfilo.setOnClickListener(goToEditProfile())
            binding.buttonLogout.setOnClickListener{viewModel.logout()}
        }else{
            //visualizzo nelle textView i dati del dottore che sono presenti all'interno di viewModel.user
            viewModel.user.observe(viewLifecycleOwner, Observer { user ->
                // Popola le TextView con i dati del dottore
                binding.tvDatiPersonali.setText("Nome: " + user.nome + "\nCognome: " + user.cognome +
                        "\nNumero di Telefono Ambulatorio: " + user.numDiTelefono + "\nCodice Fiscale: " +
                        user.codiceFiscale + "\nData di Nascita: " + user.dataDiNascita)
                binding.indAmbulatorio.setText(user.indirizzo)
            })
        }

        return binding.root
    }
    fun goToEditProfile() : View.OnClickListener?{
        return View.OnClickListener {
            startActivity(Intent(activity, ModificaProfiloActivity::class.java))
        }
    }

    private fun redirectToMaps(address: String){
        val gmmIntentUri =
            Uri.parse("geo:0,0?q=" + Uri.encode(address))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

}