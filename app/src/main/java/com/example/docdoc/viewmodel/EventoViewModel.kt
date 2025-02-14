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

    private var uriList = arrayListOf<Uri>()
    private val EMPTYURI = Uri.parse("")
    private fun pariLista(){
        var lista = _event.value?.listaFile
        if (lista != null) {
            // viene aggiunto un uri vuoto per ogni elemento già presente in lista file
            for (item in lista){
                uriList.add(EMPTYURI)
            }
        }
    }

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
            setListaFile(lista)
            uriList.add(uri)
        }
    }

    private fun setListaFile(lista: ArrayList<String>){
        _event.value?.let {
            it.listaFile = lista
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
                _event.value = evento!!
                _eventoUiState.value = EventoUiState.haveData()
                pariLista()
            }.addOnFailureListener{
                _eventoUiState.value = EventoUiState.error()
            }
    }

    /** @brief funzione che elimina l'evento passato come parametro dal Database */
    fun deleteEvent(eventId: String) {
        eventoRepository.deleteEvent(eventId)
            .addOnSuccessListener {
                storageRepository.deleteAllFiles(eventId)
                    .addOnSuccessListener {
                        _eventoUiState.value = EventoUiState.eliminated()
                    }
                    .addOnFailureListener{
                        _eventoUiState.value = EventoUiState.error()
                    }
            }.addOnFailureListener {
                _eventoUiState.value = EventoUiState.error()
            }
    }

    /** @brief funzione che controlla se i campi inseriti dall'utente sono vuoti */
    fun checkInput(): Boolean{
        var flag = false
        if (_event.value?.motivo != null && _event.value?.motivo!= "" && _event.value?.data != null && _event.value?.descrizione != null && _event.value?.descrizione != "")
            flag = true
        return flag
    }

    fun loadFiles() {
        // per ogni file si fa a effettuare l'upload sullo storage
        uriList.forEachIndexed { index, uri ->
            if(uri != EMPTYURI){
                storageRepository.uploadFile(uri, event.value?.eid!!, event.value?.listaFile!![index])
                    .addOnFailureListener {
                        _eventoUiState.value = EventoUiState.error()
                    }
            }
        }
    }

    private fun cancellaFileDallaLista(filename: String){
        var lista = _event.value?.listaFile

        try {
            val index = lista!!.indexOf(filename)
            uriList.removeAt(index)
            lista.removeAt(index)
            setListaFile(lista)
        }
        catch (e :NullPointerException){
            _eventoUiState.value = EventoUiState.error()
        }

    }

    fun deleteStorageFile(filename: String){
        if(_event.value?.eid != null){
            val eid = _event.value?.eid!!
            storageRepository.deleteFileOnStorage(eid,filename)
        }
        cancellaFileDallaLista(filename)
    }
}