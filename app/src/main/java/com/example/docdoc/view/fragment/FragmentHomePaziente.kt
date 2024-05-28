package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.FragmentHomePazienteBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.viewmodel.UtenteViewModel
import kotlinx.coroutines.launch

class FragmentHomePaziente : Fragment() {

    private lateinit var binding: FragmentHomePazienteBinding

    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePazienteBinding.inflate(inflater, container, false)

        var currentUser: Utente? = null

        viewModel.getCurrentUser()

        lifecycleScope.launch{
            viewModel.uiState.collect{
                if(it.fetchData) {
                    currentUser = viewModel.currentUser.value
                }
            }

        }

        binding.buttonProfiloMedico.setOnClickListener(goToDoctorProfile(currentUser?.uidMedico!!))

        return binding.root
    }

    private fun goToDoctorProfile(uidMedico: String): View.OnClickListener?{
        return View.OnClickListener {
            viewModel.getUser(uidMedico)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentProfiloMedico())
                .addToBackStack(null)
                .commit()
        }
    }
}