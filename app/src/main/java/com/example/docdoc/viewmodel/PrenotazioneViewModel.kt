package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.model.Prenotazione
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.PrenotazioneUiState
import com.example.docdoc.util.PrenotazioniUtil
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.docdoc.util.PrenotazioniUtil.Companion.getIdUnivoco
import com.google.firebase.firestore.toObject

class PrenotazioneViewModel : ViewModel(){

    private val firestoreRepository = FirestoreRepository()
    private val utenteRepository = UtenteRepository()

    init {
        getUserData()
    }

    //LiveData per gestire la modifica dei dati della prenotazione
    private val _prenotazione = MutableLiveData<Prenotazione>(Prenotazione())
    val prenotazione: LiveData<Prenotazione> get() = _prenotazione

    //LiveData per gestire la lista delle prenotazioni del giorno selezionato
    private val _listaPrenotazioni = MutableLiveData<ArrayList<Prenotazione>>()
    val listaPrenotazioni: LiveData<ArrayList<Prenotazione>> get() = _listaPrenotazioni

    //LiveData per recuperare l'utente corrente nel DataBase
    private val _user = MutableLiveData<Utente>()
    val user: LiveData<Utente> get() = _user

    // StateFlow per la gestione dello stato della prenotazione
    private val _prenotazioneUiState = MutableStateFlow(PrenotazioneUiState.creating())
    val prenotazioneUiState: StateFlow<PrenotazioneUiState> = _prenotazioneUiState.asStateFlow()

    fun getPrenotazione(pid: String){
        firestoreRepository.getPrenotazionePerPid(pid)
            .addOnSuccessListener {document ->
                setPrenotazione(
                    Prenotazione(
                        pid = document.id,
                        nomePaziente = document.data!!["nomePaziente"] as String?,
                        cognomePaziente = document.data!!["cognomePaziente"] as String?,
                        orario = document.data!!["orario"] as String?,
                        descrizione =document.data!!["descrizione"] as String?,
                        data = document.data!!["data"] as String?,
                    )
                )
                _prenotazioneUiState.value = PrenotazioneUiState.viewing()
            }
    }

    private fun setPrenotazione(prenotazione: Prenotazione)
    {
        _prenotazione.value = prenotazione
    }

    fun setStato(stato : PrenotazioneUiState){
        _prenotazioneUiState.value = stato
    }

    fun addPrenotazioneData(){
        var oldPid : String?
        prenotazione.value?.pid.let {
            oldPid = it
        }

        // se l'utente che prenota non è il medico allora si impostano nella prenotazione
        // sia gli uid del medico che quello del paziente
        // altrimenti si imposta solo quello del medico poichè il paziente
        // non ha effettuato la prenotazione
        if(!(_user.value?.medico!!)){
            _prenotazione.value?.uidPaziente = _user.value?.uid
            _prenotazione.value?.uidMedico = _user.value?.uidMedico
        }
        else{
            _prenotazione.value?.uidMedico = _user.value?.uid
        }

        // lo uid univoco sarà formato da data + orario + medico relativi alla prenotazione
        _prenotazione.value?.pid = getIdUnivoco(_prenotazione.value!!)
        firestoreRepository.addPrenotazioneData(_prenotazione.value!!)
            .addOnSuccessListener{
                // se la prenotazione è stata modificata si va ad eliminare quella precedente
                // dal database
                if(prenotazioneUiState.value.isEdit && oldPid != _prenotazione.value?.pid){
                    deletePrenotazione(oldPid!!)
                }
                setStato(PrenotazioneUiState.viewing())
            }
            .addOnFailureListener{
                setStato(PrenotazioneUiState.error())
            }

    }

    /**
     * @brief Funzione per ottenere le prenotazioni di un determinato giorno
     * @param giorno String che indica il giorno di cui ottenere le prenotazioni
     */
    fun getPrenotazioniPerGiorno(giorno : String){
        if(giorno.isNotEmpty()){
            val uidMedico = if (_user.value?.medico!!) _user.value?.uid!! else _user.value?.uidMedico!!
            firestoreRepository.getPrenotazioniPerGiorno(giorno, uidMedico)
                // lo snapshotlistener permette di osservare continuamente i risultati
                .addSnapshotListener { documents, _->
                    if (documents != null) {
                        // cambia la lista delle prenotazioni
                        setListaPrenotazioni(
                            PrenotazioniUtil.ordinaListaPerOrario(
                                parsePrenotazioni(
                                    documents
                                )
                            )
                        )
                    }
                }
        }
    }

    private fun setListaPrenotazioni(lista: ArrayList<Prenotazione>){
        _listaPrenotazioni.value = (_listaPrenotazioni.value ?: arrayListOf()).apply {
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
                nomePaziente = document.data["nomePaziente"] as String,
                cognomePaziente = document.data["cognomePaziente"] as String
            )
            bookingList.add(prenotazione)
        }
        return bookingList
    }

    fun deletePrenotazione(pid: String){
        firestoreRepository.deletePrenotazione(pid)
            .addOnSuccessListener {
                setStato(PrenotazioneUiState.deleted())
            }
            .addOnFailureListener{
                setStato(PrenotazioneUiState.error())
            }
    }

    private fun getUserData()
    {
        utenteRepository.getCurrentUser()
            .addOnSuccessListener {
                val utente = it.toObject<Utente>()
                _user.value = utente!!
            }
    }

    fun setOrarioOdierno(data: String, orario: String){
        _prenotazione.value?.orario = orario
        _prenotazione.value?.data = data
    }
}