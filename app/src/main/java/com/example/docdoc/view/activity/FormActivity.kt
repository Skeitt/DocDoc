package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.databinding.ActivityFormBinding
import com.example.docdoc.uistate.FormUiState
import com.example.docdoc.viewmodel.FormViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity() {
    private val viewModel: FormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // controllo dello stato dell'activity
        lifecycleScope.launch {
            viewModel.formUiState.collect{
                if(it.isError)
                {
                    Toast.makeText(this@FormActivity, "Errore nell'upload", Toast.LENGTH_SHORT)
                        .show()
                }
                if (it.isInfoStored){
                    startActivity(Intent(this@FormActivity, MainActivity::class.java))
                    finish()
                }
                if(it.isLoading) {
                    binding.navHostFragment.visibility = View.GONE
                    binding.caricamento.visibility = View.VISIBLE
                }
                else if (it == FormUiState()){
                    binding.caricamento.visibility = View.GONE
                    binding.navHostFragment.visibility = View.VISIBLE
                }
            }
        }
    }
}