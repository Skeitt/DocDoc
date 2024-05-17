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
    fun signUp()
    {
        viewModelScope.launch{
            if(signUpRepository.signUp(_user.value?.email!!, _password.value!!))
            {
                // setting dei parametri estratti dal codice fiscale nell'oggeetto _user
                setSesso(CodiceFiscaleUtil.estraiSesso(_user.value?.codiceFiscale!!))
                setDataDiNascita(CodiceFiscaleUtil.estraiDataDiNascita(_user.value?.codiceFiscale!!))
                // setting dello uid dello user appena registrato (e loggato)
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
    fun setEmail(email: String){
        // let permette di eseguire il codice solo se _user.value non è null
        // infatti non avrebbe senso aggiornare il LiveData e quindi mandare una notifica alla ui
        _user.value?.let {
            it.email = email
            // riassegna il LiveData in modo che invii una notifica alla ui
            // (non basta solamente riassegnare un parametro di _user)
            _user.value = it
        }
    }

    fun setPassword(password: String)
    {
        _password.value = password
    }

    fun setNome(nome : String)
    {
        _user.value?.let {
            it.nome = nome
            _user.value = it
        }
    }

    fun setCognome(cognome : String)
    {
        _user.value?.let {
            it.cognome = cognome
            _user.value = it
        }
    }

    fun setSesso(sesso : String)
    {
        _user.value?.let {
            it.sesso = sesso
            _user.value = it
        }
    }

    fun setNumDiTelefono(numDiTelefono : String)
    {
        _user.value?.let {
            it.numDiTelefono = numDiTelefono
            _user.value = it
        }
    }

    fun setCodiceFiscale(codiceFiscale: String) {
        _user.value?.let {
            it.codiceFiscale = codiceFiscale
            _user.value = it
        }
    }

    fun setDataDiNascita(dataDiNascita: String) {
        _user.value?.let {
            it.dataDiNascita = dataDiNascita
            _user.value = it
        }
    }

    fun setIndirizzo(indirizzo: String) {
        _user.value?.let {
            it.indirizzo = indirizzo
            _user.value = it
        }
    }
    fun setUid(uid: String) {
        _user.value?.let {
            it.uid = uid
            _user.value = it
        }
    }
    // TODO: implementare la lista dei medici e di conseguenza la seguente funzione
    fun setUidMedico(uidMedico : String)
    {
        _user.value?.let {
            it.uidMedico = uidMedico
            _user.value = it
        }
    }
}