package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.docdoc.databinding.FragmentHomePazienteBinding
import com.example.docdoc.viewmodel.UtenteViewModel

class FragmentHomePaziente : Fragment() {

    private lateinit var binding: FragmentHomePazienteBinding

    private val viewModel: UtenteViewModel by viewModels({ requireActivity() })
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePazienteBinding.inflate(inflater, container, false)

        return binding.root
    }
}