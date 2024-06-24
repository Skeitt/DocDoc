package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.repository.UtenteRepository
import com.example.docdoc.uistate.ModificaMalattieFarmaciUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ModificaMalattieFarmaciViewModel : ViewModel()  {

    private val utenteRepository = UtenteRepository()

    //LiveData per recuperare lo userId del paziente
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    //LiveData per recuperare i farmaci di un paziente
    private val _farmaci = MutableLiveData<List<String>?>()
    val farmaci: LiveData<List<String>?> get() = _farmaci

    //Livedata per recuperare le malattie di un paziente
    private val _malattie = MutableLiveData<List<String>?>()
    val malattie: LiveData<List<String>?> get() = _malattie

    // StateFlow per la gestione dello stato della modifica dei dati dell'utente
    private val _modificaUiState = MutableStateFlow(ModificaMalattieFarmaciUiState())
    val modificaUiState: StateFlow<ModificaMalattieFarmaciUiState> = _modificaUiState.asStateFlow()


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

    fun addMalattia(malattia: String) {
        val updatedMalattie = _malattie.value?.toMutableList() ?: mutableListOf()
        updatedMalattie.add(malattia)
        _malattie.value = updatedMalattie
    }

    fun addFarmaco(farmaco: String) {
        val updatedFarmaci = _farmaci.value?.toMutableList() ?: mutableListOf()
        updatedFarmaci.add(farmaco)
        _farmaci.value = updatedFarmaci
    }

    fun removeMalattia(position: Int) {
        val updatedMalattie = _malattie.value?.toMutableList() ?: mutableListOf()
        updatedMalattie.removeAt(position)
        _malattie.value = updatedMalattie
    }

    fun removeFarmaco(position: Int) {
        val updatedFarmaci = _farmaci.value?.toMutableList() ?: mutableListOf()
        updatedFarmaci.removeAt(position)
        _farmaci.value = updatedFarmaci
    }

    /** @brief funzione che aggiorna le malattie e i farmaci di uno specifico paziente all'interno del Database
     * @param userId id dell'utente di cui si vogliono aggiornare i dati*/
    fun updateCartellaClinica(userId: String) {
        val cartellaClinicaData = mapOf(
            "malattie" to _malattie.value,
            "farmaci" to _farmaci.value,
            "uidPaziente" to _userId.value
        )
        utenteRepository.updateCartellaClinica(userId, cartellaClinicaData)
            .addOnSuccessListener {
                _modificaUiState.value = ModificaMalattieFarmaciUiState.modified()
            }.addOnFailureListener{
                _modificaUiState.value = ModificaMalattieFarmaciUiState.error()
            }
    }

    fun setUserId(userId: String){
        _userId.value = userId
    }
}