package com.example.docdoc.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.docdoc.databinding.FragmentHomeMedicoBinding

class FragmentHomeMedico : Fragment() {

    private lateinit var binding: FragmentHomeMedicoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMedicoBinding.inflate(inflater, container, false)
        return binding.root
    }
}