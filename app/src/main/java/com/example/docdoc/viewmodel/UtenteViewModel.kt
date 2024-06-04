package com.example.docdoc.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.LoginRepository
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.LoginUiState
import com.example.docdoc.uistate.ModificaProfiloUiState
import com.example.docdoc.uistate.UtenteUiState
import com.example.docdoc.util.PrenotazioniUtil.Companion.ordinaListaPerOrario
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    //LiveData per gestire la modifica dei dati dell'utente
    private val _editUser = MutableLiveData<Utente>()
    val editUser: LiveData<Utente> get() = _editUser

    //LiveData per recuperare i farmaci di un paziente
    private val _farmaci = MutableLiveData<List<String>?>()
    val farmaci: LiveData<List<String>?> get() = _farmaci

    //Livedata per recuperare le malattie di un paziente
    private val _malattie = MutableLiveData<List<String>?>()
    val malattie: LiveData<List<String>?> get() = _malattie


    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _dataUiState = MutableStateFlow(UtenteUiState())
    val dataUiState: StateFlow<UtenteUiState> = _dataUiState.asStateFlow()

    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _loginUiState = MutableStateFlow(LoginUiState.loggedIn())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // StateFlow per la gestione dello stato della modifica dei dati dell'utente
    private val _modificaProfiloUiState = MutableStateFlow(ModificaProfiloUiState())
    val modificaProfiloUiState: StateFlow<ModificaProfiloUiState> = _modificaProfiloUiState.asStateFlow()


    init {
        getPrenotazioniPerGiorno(giorno = "2024-05-31")
        getListaPazienti()
    }

    /** @brief funzione che aggiorna il LiveData currentUser una volta che recupera l'utente nel Database */
    fun getCurrentUser(){
        viewModelScope.launch {
            utenteRepository.getCurrentUser()
                .addOnSuccessListener {
                    val utente = it.toObject<Utente>()
                    _currentUser.value = utente!!
                    _dataUiState.value = UtenteUiState.haveData()
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
                indirizzo = document.data["indirizzo"] as String?
            )
        listaPazienti.add(utente)
        }
        return listaPazienti
    }

    /** @brief funzione che aggiorna il LiveData editUser una volta che recupera i dati dell'utente di cui si
     * vogliono modificare i dati */
    fun getCurrentUserToEditData(){
        viewModelScope.launch {
            utenteRepository.getCurrentUser()
                .addOnSuccessListener {
                    val utente = it.toObject<Utente>()
                    _editUser.value = utente!!
                }
                .addOnFailureListener{
                }
        }
    }

    /** @brief funzione che aggiorna il Database e il LiveData currentUser con i nuovi dati dell'utente */
    fun updateCurrentUserData(){
        viewModelScope.launch {
            utenteRepository.updateCurrentUserData(_editUser.value!!)
                .addOnSuccessListener {
                    _modificaProfiloUiState.value = ModificaProfiloUiState.modified()
                    _currentUser.value = _editUser.value!!
                }.addOnFailureListener {
                    _modificaProfiloUiState.value = ModificaProfiloUiState.error()
                }
        }
    }

    /** @brief funzione che elimina l'utente corrente dal Database */
    fun deleteCurrentUser() {
        viewModelScope.launch {
            //caso in cui viene eliminato un medico
            if (_currentUser.value!!.medico!!) {
                //setto a null il campo uidMedico di tutti i pazienti che hanno come medico l'utente che si sta eliminando
                utenteRepository.updatePatientsUidMedicoField(_currentUser.value!!.uid!!)
                    .addOnSuccessListener {
                        //elimino il documento dell'utente nel database firestore
                        utenteRepository.deleteCurrentUser()
                            .addOnSuccessListener {
                                //elimino il profilo di autenticazione dell'utente
                                utenteRepository.deleteAuthenticationCurrentUser()
                                    .addOnSuccessListener {
                                        _modificaProfiloUiState.value = ModificaProfiloUiState.eliminated()
                                    }.addOnFailureListener{
                                        _modificaProfiloUiState.value = ModificaProfiloUiState.error()
                                    }
                            }.addOnFailureListener{
                                _modificaProfiloUiState.value = ModificaProfiloUiState.error()
                            }
                    }.addOnFailureListener {
                        _modificaProfiloUiState.value = ModificaProfiloUiState.error()
                    }
            } else {
                //caso in cui viene eliminato un paziente
                //elimino il documento dell'utente nel database firestore
                utenteRepository.deleteCurrentUser()
                    .addOnSuccessListener {
                        //elimino il profilo di autenticazione dell'utente
                        utenteRepository.deleteAuthenticationCurrentUser()
                            .addOnSuccessListener {
                                _modificaProfiloUiState.value = ModificaProfiloUiState.eliminated()
                            }.addOnFailureListener{
                                _modificaProfiloUiState.value = ModificaProfiloUiState.error()
                            }
                    }.addOnFailureListener{
                        _modificaProfiloUiState.value = ModificaProfiloUiState.error()
                    }
            }
        }
    }

    /** @brief funzione che recupera le malattie e i farmaci di uno specifico paziente
     * @param userId id dell'utente di cui si vogliono recuperare i dati*/
    fun fetchMalattieFarmaciPaziente(userId: String){
        utenteRepository.getCartellaClinica(userId)
            .addOnSuccessListener { document ->
                val malattie = document.data?.get("malattie") as? List<String>
                val farmaci = document.data?.get("farmaci") as? List<String>
                if (malattie != null){
                    _malattie.value = malattie
                }else{
                    _malattie.value = null
                }
                if (farmaci != null){
                    _farmaci.value = farmaci
                }else{
                    _farmaci.value = null
                }
            }.addOnFailureListener{
            }
    }

    fun setNome(nome: String) {
        _editUser.value!!.nome = nome
    }
    fun setCognome(cognome: String) {
        _editUser.value!!.cognome = cognome
    }
    fun setCF(cf: String) {
        _editUser.value!!.codiceFiscale = cf
    }
    fun setTelefono(telefono: String) {
        _editUser.value!!.numDiTelefono = telefono
    }
    fun setIndirizzo(indirizzo: String) {
        _editUser.value!!.indirizzo = indirizzo
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