package com.example.docdoc.uistate

data class FormUiState(
    val isInfoStored: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        fun pushSuccess() = FormUiState(isInfoStored = true)
        fun infoFound() = FormUiState(isInfoStored = true)
        fun error() = FormUiState(isError = true)
    }
}