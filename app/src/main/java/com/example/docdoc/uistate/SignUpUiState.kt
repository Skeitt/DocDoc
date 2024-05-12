package com.example.docdoc.uistate

data class SignUpUiState(
    val isSignedUp: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        fun success() = SignUpUiState(isSignedUp = true)
        fun error(flag: Boolean) = SignUpUiState(isError = flag)
    }
}