package com.example.docdoc.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.docdoc.databinding.ActivityModificaMalattieFarmaciBinding
import com.example.docdoc.viewmodel.ModificaMalattieFarmaciViewModel

class ModificaMalattieFarmaciActivity: AppCompatActivity() {

    private val viewModel: ModificaMalattieFarmaciViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityModificaMalattieFarmaciBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.indietroButton.setOnClickListener(goBack())
        binding.salvaButton.setOnClickListener(salvaDati())

    }

    private fun goBack() : View.OnClickListener?{
        return View.OnClickListener {
            //torno al fragment del profilo del paziente
            finish()
        }
    }

    private fun salvaDati() : View.OnClickListener?{
        return View.OnClickListener {

        }
    }

}