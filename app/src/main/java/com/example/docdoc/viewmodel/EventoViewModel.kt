package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.model.Evento
import com.example.docdoc.repository.EventoRepository
import com.example.docdoc.uistate.EventoUiState
import com.example.docdoc.util.EventoUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventoViewModel : ViewModel() {

    private val eventoRepository = EventoRepository()
    private val eventoUtil = EventoUtil

    //LiveData per l'inserimento dell'evento nel Database
    private val _event = MutableLiveData<Evento>(Evento())
    val event: LiveData<Evento> get() = _event

    // StateFlow per la gestione dello stato dell'evento legato all'utente
    private val _eventoUiState = MutableStateFlow(EventoUiState())
    val eventoUiState: StateFlow<EventoUiState> = _eventoUiState.asStateFlow()


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
            it.eid = eventoUtil.generateId()
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

}