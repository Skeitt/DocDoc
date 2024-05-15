package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.SignUpRepository
import com.example.docdoc.uistate.SignUpUiState
import com.example.docdoc.util.CodiceFiscaleUtil
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    // util
    private val cfUtil = CodiceFiscaleUtil()

    // repository
    private val signUpRepository = SignUpRepository()
    private val firestoreRepository = FirestoreRepository()

    // LiveData per il form di registrazione
    private val _user = MutableLiveData<Utente>(Utente())
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
        _user.value?.dataDiNascita = dataDiNascita
    }

    fun setIndirizzo(indirizzo : String)
    {
        _user.value?.indirizzo = indirizzo
    }

    fun setUid(uid : String)
    {
        _user.value?.uid = uid
    }

    // TODO: implementare la lista dei medici e di conseguenza la seguente funzione
    fun setUidMedico(uidMedico : String)
    {
        _user.value?.uidMedico = uidMedico
    }
    fun signUp()
    {
        viewModelScope.launch{
            if(signUpRepository.signUp(_user.value?.email!!, _password.value!!))
            {
                // setting dei parametri estratti dal codice fiscale
                setSesso(cfUtil.estraiSesso(_user.value?.codiceFiscale!!))
                setDataDiNascita(cfUtil.estraiDataDiNascita(_user.value?.dataDiNascita!!))
                setUid(signUpRepository.getCurrentUserUid())
                // inserimento dei dati dell'utente su Firestore
                if (firestoreRepository.addUserData(_user.value!!))
                {
                    _signupUiState.value = SignUpUiState.success()
                }
                else
                {
                    _signupUiState.value = SignUpUiState.error()
                    // TODO: va eliminato l'utente in authentication
                }
            }
            else
            {
                // se l'autenticazione non va a buon fine
                _signupUiState.value = SignUpUiState.error()
            }
        }
    }
}