package com.example.docdoc.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.docdoc.R
import com.example.docdoc.databinding.ActivityModificaProfiloBinding
import com.example.docdoc.model.Utente
import com.example.docdoc.view.fragment.FragmentModificaMedico
import com.example.docdoc.view.fragment.FragmentModificaPaziente
import com.example.docdoc.viewmodel.UtenteViewModel
import kotlinx.coroutines.launch

class ModificaProfiloActivity: AppCompatActivity() {

    private val viewModel: UtenteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityModificaProfiloBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var currentUser: Utente? = null

        viewModel.getCurrentUser()
        lifecycleScope.launch{
            viewModel.dataUiState.collect{
                if(it.fetchData) {
                    currentUser = viewModel.currentUser.value
                    viewModel.getCurrentUserToEditData()
                    if (currentUser?.medico!!){
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentModificaMedico())
                            .commit()
                    }else{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FragmentModificaPaziente())
                            .commit()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.modificaProfiloUiState.collect{
                if(it.isModified){
                    //torno al fragment del profilo
                    finish()
                }
                if (it.isError){
                    //visualizzo un messaggio di errore
                    Toast.makeText(this@ModificaProfiloActivity, "Errore", Toast.LENGTH_SHORT)
                        .show()
                }
                if(it.isEliminated){
                    //chiudo tutte le activity e torno al login
                    val intent = Intent(this@ModificaProfiloActivity, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                }
            }
        }

        binding.indietroButton.setOnClickListener(goBack())
        binding.continuaButton.setOnClickListener(salvaDati())
        binding.eliminaAccountButton.setOnClickListener(eliminaAccount())

    }

    private fun goBack() : View.OnClickListener?{
        return View.OnClickListener {
            //torno al fragment del profilo
            finish()
        }
    }

    private fun salvaDati() : View.OnClickListener?{
        return View.OnClickListener {
            viewModel.updateCurrentUserData()

        }
    }

    private fun eliminaAccount() : View.OnClickListener?{
        return View.OnClickListener {
            viewModel.deleteCurrentUser()
        }
    }

}