package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.docdoc.databinding.FragmentModificaEventoBinding
import com.example.docdoc.viewmodel.EventoViewModel

class FragmentModificaEvento : Fragment() {

    private lateinit var binding: FragmentModificaEventoBinding
    private val viewModel: EventoViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModificaEventoBinding.inflate(inflater, container, false)

        return binding.root
    }
}