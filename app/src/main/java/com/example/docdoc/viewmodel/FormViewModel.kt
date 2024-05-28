package com.example.docdoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.docdoc.model.Utente
import com.example.docdoc.repository.FirestoreRepository
import com.example.docdoc.repository.SignUpRepository
import com.example.docdoc.uistate.FormUiState
import com.example.docdoc.util.CodiceFiscaleUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FormViewModel : ViewModel() {

    // repository
    private val signUpRepository = SignUpRepository()
    private val firestoreRepository = FirestoreRepository()

    // LiveData per lo stato dell'UI
    // StateFlow per la gestione dello stato del login
    private val _formUiState = MutableStateFlow(FormUiState())
    val formUiState: StateFlow<FormUiState> = _formUiState.asStateFlow()

    // medici
    private val _medici = MutableLiveData<ArrayList<Utente>>()
    val medici: LiveData<ArrayList<Utente>> get() = _medici

    // LiveData per il form di registrazione
    private val _user = MutableLiveData<Utente>(Utente())
    val user: LiveData<Utente> get() = _user

    fun pushUserData()
    {
        if(_user.value?.ruolo == "medico")
        {
            setUidMedico("")
        }
        // setting dei parametri estratti dal codice fiscale nell'oggeetto _user
        setSesso(CodiceFiscaleUtil.estraiSesso(_user.value?.codiceFiscale!!))
        setDataDiNascita(CodiceFiscaleUtil.estraiDataDiNascita(_user.value?.codiceFiscale!!))
        // setting dello uid dello user appena registrato (e loggato)
        setUid(signUpRepository.getCurrentUserUid())
        setEmail(signUpRepository.getCurrentUserEmail())
        // inserimento dei dati dell'utente su Firestore
        firestoreRepository.addUserData(_user.value!!)
            .addOnSuccessListener {
                _formUiState.value = FormUiState.pushSuccess()
            }.addOnFailureListener {
                _formUiState.value = FormUiState.error()
            }
    }

    fun updateDoctorsList(){
        firestoreRepository.getDoctorList()
            .addOnSuccessListener { documents ->
                val doctorsList = ArrayList<Utente>()
                for (document in documents) {
                    val medico = Utente(
                        uid = document.id,
                        nome = document.data["nome"] as String?,
                        cognome = document.data["cognome"] as String?,
                        indirizzo = document.data["indirizzo"] as String?
                    )
                    doctorsList.add(medico)
                }
                /** la lista dei medici è stata recuperata */
                _medici.value = doctorsList // Assegna la lista di medici a _medici
            }
            .addOnFailureListener {
                /** Get della lista dei medici non andata a buon fine */
                _formUiState.value = FormUiState.error()
            }
    }

    /**
     * @brief La funzione cambia lo stato della ui:
     *  - se l'utente loggato ha compilato il form allora cambia stato
     *  - se l'utente loggato non ha compilato il form deve compilarlo
     */
    fun isInfoStored(){
        val uid = signUpRepository.getCurrentUserUid()
        firestoreRepository.isInfoStored(uid)
            .addOnSuccessListener {document ->
                if(document.exists())
                {
                    _formUiState.value = FormUiState.infoFound()
                }
            }
            .addOnFailureListener{
                _formUiState.value = FormUiState()
            }
    }

    fun setNome(nome: String) {
        _user.value?.let {
            it.nome = nome
            _user.value = it
        }
    }

    fun setCognome(cognome: String) {
        _user.value?.let {
            it.cognome = cognome
            _user.value = it
        }
    }

    fun setEmail(email : String)
    {
        _user.value?.let {
            it.email = email
            _user.value= it
        }
    }

    fun setSesso(sesso: String) {
        _user.value?.let {
            it.sesso = sesso
            _user.value = it
        }
    }

    fun setNumDiTelefono(numDiTelefono: String) {
        _user.value?.let {
            it.numDiTelefono = numDiTelefono
            _user.value = it
        }
    }

    fun setCodiceFiscale(codiceFiscale: String) {
        _user.value?.let {
            it.codiceFiscale = codiceFiscale
            _user.value = it
        }
    }

    fun setDataDiNascita(dataDiNascita: String) {
        _user.value?.let {
            it.dataDiNascita = dataDiNascita
            _user.value = it
        }
    }

    fun setIndirizzo(indirizzo: String) {
        _user.value?.let {
            it.indirizzo = indirizzo
            _user.value = it
        }
    }

    fun setUid(uid: String) {
        _user.value?.let {
            it.uid = uid
            _user.value = it
        }
    }

    fun setRuolo(ruolo: String) {
        _user.value?.let {
            it.ruolo = ruolo
            _user.value = it
        }
    }

    fun setUidMedico(uidMedico: String) {
        _user.value?.let {
            it.uidMedico = uidMedico
            _user.value = it
        }
    }

}