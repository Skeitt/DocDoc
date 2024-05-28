package com.example.docdoc.uistate

data class SignUpUiState(
    val isSignedUp: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        fun authSuccess() = SignUpUiState(isSignedUp = true)
        fun authError() = SignUpUiState(isError = true)
    }
}