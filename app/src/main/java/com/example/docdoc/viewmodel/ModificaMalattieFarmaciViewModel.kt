package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.repository.UtenteRepository

class ModificaMalattieFarmaciViewModel : ViewModel()  {

    private val utenteRepository = UtenteRepository()

    //LiveData per recuperare i farmaci di un paziente
    private val _farmaci = MutableLiveData<List<String>?>()
    val farmaci: LiveData<List<String>?> get() = _farmaci

    //Livedata per recuperare le malattie di un paziente
    private val _malattie = MutableLiveData<List<String>?>()
    val malattie: LiveData<List<String>?> get() = _malattie

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
}