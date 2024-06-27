package com.example.docdoc.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.model.Evento
import com.example.docdoc.repository.EventoRepository
import com.example.docdoc.repository.StorageRepository
import com.example.docdoc.uistate.EventoUiState
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventoViewModel : ViewModel() {

    private val eventoRepository = EventoRepository()
    private val storageRepository = StorageRepository()

    //LiveData per l'inserimento dell'evento nel Database
    private val _event = MutableLiveData<Evento>(Evento())
    val event: LiveData<Evento> get() = _event

    //LiveData per recuperare l'evento da modificare
    private val _editEvent = MutableLiveData<Evento>()
    val editEvent: LiveData<Evento> get() = _editEvent

    private var uriList = arrayListOf<Uri>()



    // StateFlow per la gestione dello stato dell'evento legato all'utente
    private val _eventoUiState = MutableStateFlow(EventoUiState())
    val eventoUiState: StateFlow<EventoUiState> = _eventoUiState.asStateFlow()


    fun addFile(filename: String, uri: Uri){
        var lista = _event.value?.listaFile
        if (lista == null) {
            lista = arrayListOf()
        }

        //se la lista contiene già il file con lo stesso nome non viene caricato
        if (!lista.contains(filename)) {
            lista.add(filename)
            _event.value?.let{
                it.listaFile = lista
                _event.value = it
                uriList.add(uri)
            }
        }
    }


    fun setMotivo(motivo: String) {
        _event.value?.let {
            it.motivo = motivo
            _event.value = it
        }
    }
    fun setDescrizione(descrizione: String) {
        _event.value?.let {
            it.descrizione = descrizione
            _event.value = it
        }
    }
    fun setData(data: String) {
        _event.value?.let {
            it.data = data
            _event.value = it
        }
    }
    fun setEventID(){
        _event.value?.let {
            it.eid = eventoRepository.generateId()
            _event.value = it
        }
    }
    fun setUidPaziente(uid: String){
        _event.value?.let {
            it.uidPaziente = uid
            _event.value = it
        }
    }
    fun setMotivoToEditEvent(motivo: String) {
        _editEvent.value!!.motivo = motivo
    }
    fun setDescrizioneToEditEvent(descrizione: String) {
        _editEvent.value!!.descrizione = descrizione
    }
    fun setDataToEditEvent(data: String) {
        _editEvent.value!!.data = data
    }

    /** @brief funzione che inserisce l'evento nel Database */
    fun setEventData(){
        eventoRepository.setEventData(_event.value!!)
            .addOnSuccessListener {
                _eventoUiState.value = EventoUiState.created()
            }.addOnFailureListener {
                _eventoUiState.value = EventoUiState.error()
            }
    }

    /** @brief funzione che aggiorna il LiveData editEvent una volta che recupera i dati dell'evento di cui si
     * vogliono modificare i dati */
    fun getEventToEditData(eventId: String){
        eventoRepository.getEventData(eventId)
            .addOnSuccessListener {
                val evento = it.toObject<Evento>()
                _editEvent.value = evento!!
                _eventoUiState.value = EventoUiState.haveData()
            }.addOnFailureListener{
                _eventoUiState.value = EventoUiState.error()
            }
    }

    /** @brief funzione che aggiorna il Database e il LiveData currentUser con i nuovi dati dell'utente */
    fun updateEventData(){
        eventoRepository.updateEventData(_editEvent.value!!)
            .addOnSuccessListener {
                _eventoUiState.value = EventoUiState.modified()
            }.addOnFailureListener {
                _eventoUiState.value = EventoUiState.error()
            }
    }

    /** @brief funzione che elimina l'evento passato come parametro dal Database */
    fun deleteEvent(eventId: String) {
        eventoRepository.deleteEvent(eventId)
            .addOnSuccessListener {
                _eventoUiState.value = EventoUiState.eliminated()
            }.addOnFailureListener {
                _eventoUiState.value = EventoUiState.error()
            }
    }

    /** @brief funzione che controlla se i campi inseriti dall'utente sono vuoti */
    fun checkInput(): Boolean{
        var flag = false
        if (_event.value?.motivo != null && _event.value?.data != null && _event.value?.descrizione != null)
            flag = true
        return flag
    }

    /** @brief funzione che controlla se i campi inseriti dall'utente sono vuoti */
    fun checkInputToEditEvent(): Boolean{
        var flag = false
        if (_editEvent.value?.motivo != "" && _editEvent.value?.data != "" && _editEvent.value?.descrizione != "")
            flag = true
        return flag
    }

    fun loadFiles() {
        // per ogni file si fa a effettuare l'upload sullo storage
        uriList.forEachIndexed { index, uri ->
            storageRepository.uploadFile(uri, event.value?.eid!!, event.value?.listaFile!![index])
                .addOnFailureListener {
                    _eventoUiState.value = EventoUiState.error()
                }
        }
    }
}