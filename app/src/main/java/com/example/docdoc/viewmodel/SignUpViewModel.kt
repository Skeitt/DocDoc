package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.SignUpRepository
import com.example.docdoc.uistate.SignUpUiState
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    // repository
    private val signUpRepository = SignUpRepository()

    // LiveData per il form di registrazione
    private val _user = MutableLiveData<Utente>()
    val user: LiveData<Utente> get() = _user

    // LiveData per lo stato dell'UI
    private val _signupUiState = MutableLiveData<SignUpUiState>()
    val signupUiState: LiveData<SignUpUiState> get() = _signupUiState

    // LiveData per la password
    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    fun setEmail(email: String){
        _user.value?.email = email
    }

    fun setPassword(password: String)
    {
        _password.value = password
    }

    fun setNome(nome : String)
    {
        _user.value?.nome = nome
    }

    fun setCognome(cognome : String)
    {
        _user.value?.cognome = cognome
    }

    fun setSesso(sesso : String)
    {
        _user.value?.sesso = sesso
    }

    fun setNumDiTelefono(numDiTelefono : String)
    {
        _user.value?.numDiTelefono = numDiTelefono
    }

    fun setCodiceFiscale(codiceFiscale : String)
    {
        _user.value?.codiceFiscale = codiceFiscale
    }

    fun setDataDiNascita(dataDiNascita : String)
    {
        user.value?.dataDiNascita = dataDiNascita
    }

    fun setIndirizzo(indirizzo : String)
    {
        _user.value?.indirizzo = indirizzo
    }

    fun getUid()
    {
        // TODO: accedi al database e assegna lo uid
    }
    fun signUp()
    {
        viewModelScope.launch{
            if(signUpRepository.signUp(_user.value!!.email, _password.value!! ))
            {
                // TODO: memorizzare l'utente con i suoi dati nel database
            }
            else
            {
                _signupUiState.value = SignUpUiState.error(true)
            }
        }
    }
}