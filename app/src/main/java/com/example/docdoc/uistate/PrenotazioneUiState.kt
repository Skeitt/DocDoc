package com.example.docdoc.uistate


data class PrenotazioneUiState (
    val isCreating: Boolean = false,
    val isEdit: Boolean = false,
    val isView: Boolean = false,
    val isError: Boolean = false,
){
    companion object{
        fun editing() = PrenotazioneUiState(isEdit = true)
        fun creating() = PrenotazioneUiState(isCreating = true)
        fun viewing() = PrenotazioneUiState(isView = true)
        fun error() = PrenotazioneUiState(isError = true)
        fun deleted() = PrenotazioneUiState()
    }
}