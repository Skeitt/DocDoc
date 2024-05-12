package com.example.docdoc.util

/** Classe che mette a disposizione i metodi per la validazione dei dati che vengono
    inseriti dall'utente */
class InputValidator() {

    /** funzione che verifica se l'email è valida */
    fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")
        return email.isNotEmpty() && regex.matches(email)
    }

    /** funzione che verifica se la password è valida.
        La password deve contenere:
        - almeno un carattere maiuscolo (A-Z)
        - almeno un carattere minuscolo (a-z)
        - almeno un numero (0-9)
        - almeno un carattere speciale tra @, $, %, ^, &, +, = e .
        La password non deve contenere spazi bianchi
        La password deve essere lunga almeno 8 caratteri  */
    fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=.])(?=\\S+\$).{8,}\$")
        return password.isNotEmpty() && regex.matches(password)
    }
}