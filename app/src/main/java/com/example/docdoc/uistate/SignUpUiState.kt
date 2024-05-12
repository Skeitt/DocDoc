package com.example.docdoc.uistate

data class SignUpUiState(
    val isSignedUp: Boolean = false,
    val error: String? = null,
) {
    companion object {
        fun success() = SignUpUiState(isSignedUp = true)
        fun error(toString: String) = SignUpUiState(error = toString)
    }
}