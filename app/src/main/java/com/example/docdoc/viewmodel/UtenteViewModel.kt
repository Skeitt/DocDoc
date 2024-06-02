package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.UtenteUiState
import com.example.docdoc.util.PrenotazioniUtil.Companion.ordinaListaPerOrario
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UtenteViewModel : ViewModel() {

    private val utenteRepository = UtenteRepository()
    private val firestoreRepository = FirestoreRepository()

    //LiveData per recuperare l'utente corrente nel DataBase
    private val _currentUser = MutableLiveData<Utente>()
    val currentUser: LiveData<Utente> get() = _currentUser

    //LiveData per recuperare l'utente nel DataBase
    private val _user = MutableLiveData<Utente>()
    val user: LiveData<Utente> get() = _user

    //LiveData per la lista di prenotazioni
    private val _listaPrenotazioni = MutableLiveData<ArrayList<Prenotazione>>()
    val listaPrenotazioni: LiveData<ArrayList<Prenotazione>> get() = _listaPrenotazioni


    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _uiState = MutableStateFlow(UtenteUiState())
    val uiState: StateFlow<UtenteUiState> = _uiState.asStateFlow()

    init {
        getPrenotazioniPerGiorno(giorno = "2024-05-31")
    }

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
     * @param userId id dell'utente di cui si vogliono recuperare i dati
     */
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

    /**
     * @brief Funzione per ottenere le prenotazioni di un determinato giorno
     * @param giorno String che indica il giorno di cui ottenere le prenotazioni
     */
    fun getPrenotazioniPerGiorno(giorno : String){
        firestoreRepository.getPrenotazioniPerGiorno(giorno)
            .addOnSuccessListener { documents ->
                val bookingList = ArrayList<Prenotazione>()
                for (document in documents) {
                    val prenotazione = Prenotazione(
                        pid = document.id,
                        orario = document.data["orario"] as String,
                        data = document.data["data"] as String,
                        descrizione = document.data["descrizione"] as String,
                        nomePaziente = document.data["nome"] as String,
                        cognomePaziente = document.data["cognome"] as String
                    )
                    bookingList.add(prenotazione)
                }
                // cambia la lista delle prenotazioni
                setListaPrenotazioni(ordinaListaPerOrario(bookingList))
            }
            // TODO: aggiungere l'onfailureListener
    }

    fun setListaPrenotazioni(lista: ArrayList<Prenotazione>){
        _listaPrenotazioni.value = (_listaPrenotazioni.value ?: arrayListOf()).apply {
            clear()
            addAll(lista)
        }
    }

}