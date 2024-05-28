package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.UtenteUiState
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UtenteViewModel : ViewModel() {

    private val utenteRepository = UtenteRepository()

    //LiveData per recuperare l'utente corrente nel DataBase
    private val _currentUser = MutableLiveData<Utente>()
    val currentUser: LiveData<Utente> get() = _currentUser

    //LiveData per recuperare l'utente nel DataBase
    private val _user = MutableLiveData<Utente>()
    val user: LiveData<Utente> get() = _user

    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _uiState = MutableStateFlow(UtenteUiState())
    val uiState: StateFlow<UtenteUiState> = _uiState.asStateFlow()

    /** @brief funzione che aggiorna il LiveData currentUser una volta che recupera l'utente nel Database */
    fun getCurrentUser(){
        viewModelScope.launch {
            utenteRepository.getCurrentUser()
                .addOnSuccessListener {
                    val utente = it.toObject<Utente>()
                    _currentUser.value = utente!!
                    _uiState.value = UtenteUiState.haveData()
                }
                .addOnFailureListener{
                }
        }
    }

    /** @brief funzione che aggiorna il LiveData user una volta che recupera l'utente specificato nel Database
     * @param userId id dell'utente di cui si vogliono recuperare i dati */
    fun getUser(userId : String) {
        viewModelScope.launch {
            utenteRepository.getUser(userId)
                .addOnSuccessListener {
                    val utente = it.toObject<Utente>()
                    _user.value = utente!!
                }
                .addOnFailureListener{
                }
        }
    }
}