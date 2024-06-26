package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.ModificaProfiloUiState
import com.example.docdoc.uistate.UtenteUiState
import com.example.docdoc.util.InputValidator
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ModificaProfiloViewModel : ViewModel() {

    private val utenteRepository = UtenteRepository()
    private val inputValidator = InputValidator()


    //LiveData per gestire la modifica dei dati dell'utente
    private val _editUser = MutableLiveData<Utente>()
    val editUser: LiveData<Utente> get() = _editUser

    // StateFlow per la gestione dello stato dei dati dell'utente
    private val _dataUiState = MutableStateFlow(UtenteUiState())
    val dataUiState: StateFlow<UtenteUiState> = _dataUiState.asStateFlow()

    // StateFlow per la gestione dello stato della modifica dei dati dell'utente
    private val _modificaProfiloUiState = MutableStateFlow(ModificaProfiloUiState())
    val modificaProfiloUiState: StateFlow<ModificaProfiloUiState> = _modificaProfiloUiState.asStateFlow()


    /** @brief funzione che aggiorna il LiveData editUser una volta che recupera i dati dell'utente di cui si
     * vogliono modificare i dati */
    fun getCurrentUserToEditData(){
        utenteRepository.getCurrentUser()
            .addOnSuccessListener {
                val utente = it.toObject<Utente>()
                _editUser.value = utente!!
                _dataUiState.value = UtenteUiState.haveData()
            }.addOnFailureListener{
            }
    }


    /** @brief funzione che aggiorna il Database e il LiveData currentUser con i nuovi dati dell'utente */
    fun updateCurrentUserData(){
        utenteRepository.updateCurrentUserData(_editUser.value!!)
            .addOnSuccessListener {
                _modificaProfiloUiState.value = ModificaProfiloUiState.modified()
            }.addOnFailureListener {
                _modificaProfiloUiState.value = ModificaProfiloUiState.error()
            }
    }


    /** @brief funzione che elimina l'utente corrente dal Database */
    fun deleteCurrentUser() {
        //caso in cui viene eliminato un medico
        if (_editUser.value!!.medico!!) {
            //setto a null il campo uidMedico di tutti i pazienti che hanno come medico l'utente che si sta eliminando
            utenteRepository.updatePatientsUidMedicoField(_editUser.value!!.uid!!)
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

    fun checkInput(): Boolean{
        var flag = false
        if (_editUser.value?.nome != "" && _editUser.value?.cognome != "" && _editUser.value?.codiceFiscale != ""
            && inputValidator.isValidCodiceFiscale(_editUser.value?.codiceFiscale.toString()) &&
            _editUser.value?.numDiTelefono != "" && inputValidator.isValidNumeroDiTelefono(_editUser.value?.numDiTelefono.toString())
            && _editUser.value?.indirizzo != "" && inputValidator.isValidIndirizzo(_editUser.value?.indirizzo.toString()))
            flag = true
        return flag
    }

}