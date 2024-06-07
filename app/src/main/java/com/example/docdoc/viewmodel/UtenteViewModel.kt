package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.LoginRepository
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.LoginUiState
import com.example.docdoc.uistate.UtenteUiState
import com.example.docdoc.util.PrenotazioniUtil.Companion.ordinaListaPerOrario
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UtenteViewModel : ViewModel() {

    private val utenteRepository = UtenteRepository()
    private val firestoreRepository = FirestoreRepository()
    private val loginRepository = LoginRepository()

    //LiveData per recuperare l'utente corrente nel DataBase
    private val _currentUser = MutableLiveData<Utente>()
    val currentUser: LiveData<Utente> get() = _currentUser

    //LiveData per recuperare l'utente nel DataBase
    private val _user = MutableLiveData<Utente>()
    val user: LiveData<Utente> get() = _user

    //LiveData per la lista di prenotazioni
    private val _listaPrenotazioni = MutableLiveData<ArrayList<Prenotazione>>()
    val listaPrenotazioni: LiveData<ArrayList<Prenotazione>> get() = _listaPrenotazioni

    // pazienti
    private val _pazienti = MutableLiveData<ArrayList<Utente>>()
    val pazienti: LiveData<ArrayList<Utente>> get() = _pazienti

    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _dataUiState = MutableStateFlow(UtenteUiState())
    val dataUiState: StateFlow<UtenteUiState> = _dataUiState.asStateFlow()

    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _loginUiState = MutableStateFlow(LoginUiState.loggedIn())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()


    init {
        getPrenotazioniPerGiorno(giorno = "2024-05-31")
        getListaPazienti()
    }

    /** @brief funzione che aggiorna il LiveData currentUser una volta che recupera l'utente nel Database */
    fun getCurrentUser(){
        utenteRepository.getCurrentUser()
            .addOnSuccessListener {
                val utente = it.toObject<Utente>()
                _currentUser.value = utente!!
                _dataUiState.value = UtenteUiState.haveData()
            }
            .addOnFailureListener{
            }
    }

    /** @brief funzione che aggiorna il LiveData user una volta che recupera l'utente specificato nel Database
     * @param userId id dell'utente di cui si vogliono recuperare i dati
     */
    fun getUser(userId : String) {
        utenteRepository.getUser(userId)
            .addOnSuccessListener {
                val utente = it.toObject<Utente>()
                _user.value = utente!!
            }
            .addOnFailureListener{
            }
    }

    /**
     * @brief Funzione per ottenere le prenotazioni di un determinato giorno
     * @param giorno String che indica il giorno di cui ottenere le prenotazioni
     */
    fun getPrenotazioniPerGiorno(giorno : String){
        firestoreRepository.getPrenotazioniPerGiorno(giorno)
            // lo snapshotlistener permette di osservare continuamente i risultati
            .addSnapshotListener { documents, _->
                if (documents != null) {
                    // cambia la lista delle prenotazioni
                    setListaPrenotazioni(ordinaListaPerOrario(parsePrenotazioni(documents)))
                }
            }
    }

    fun getListaPazienti()
    {
        utenteRepository.getListaPazienti()
            .addSnapshotListener{ documents, _->
                if(documents != null)
                {
                    // aggiorno la lista dei pazienti
                    setListaPazienti(parseUtenti(documents))
                }
            }
    }

    private fun setListaPrenotazioni(lista: ArrayList<Prenotazione>){
        _listaPrenotazioni.value = (_listaPrenotazioni.value ?: arrayListOf()).apply {
            clear()
            addAll(lista)
        }
    }

    private fun setListaPazienti(lista: ArrayList<Utente>){
        _pazienti.value = (_pazienti.value ?: arrayListOf()).apply {
            clear()
            addAll(lista)
        }
    }

    private fun parsePrenotazioni(documents: QuerySnapshot): ArrayList<Prenotazione>
    {
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
        return bookingList
    }

    private fun parseUtenti(documents: QuerySnapshot) :ArrayList<Utente>
    {
        val listaPazienti = ArrayList<Utente>()
        for(document in documents)
        {
            val utente = Utente(
                uid = document.id,
                nome = document.data["nome"] as String?,
                cognome = document.data["cognome"] as String?,
                indirizzo = document.data["indirizzo"] as String?,
                codiceFiscale = document.data["codiceFiscale"] as String?,
                dataDiNascita = document.data["dataDiNascita"] as String?,
                numDiTelefono = document.data["numDiTelefono"] as String?,
            )
        listaPazienti.add(utente)
        }
        return listaPazienti
    }

    fun setUser(user: Utente){
        _user.value = user
    }

    fun logout()
    {
        loginRepository.logout()
        _loginUiState.value = LoginUiState.logOut()
    }
}