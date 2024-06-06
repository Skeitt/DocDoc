package com.example.docdoc.util

/** Classe che mette a disposizione i metodi per la validazione dei dati che vengono
    inseriti dall'utente */
class InputValidator() {

    /** @brief funzione che verifica se l'email è valida
        * @param email Indica l'email di cui verificare la sintassi
        * @return restituisce True nel caso email soddisfi il pattern */
    fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$")
        return email.isNotEmpty() && regex.matches(email)
    }

    /** @brief funzione che verifica se la password è valida.
        La password deve contenere:
        - almeno un carattere maiuscolo (A-Z)
        - almeno un carattere minuscolo (a-z)
        - almeno un numero (0-9)
        - almeno un carattere speciale tra @, $, %, ^, &, +, = e .
        La password non deve contenere spazi bianchi
        La password deve essere lunga almeno 8 caratteri
        * @param password Indica la password da verificare
        * @return restituisce True nel caso in cui la password rispetti il pattern e non sia vuota*/
    fun isValidPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=.])(?=\\S+\$).{8,}\$")
        return password.isNotEmpty() && regex.matches(password)
    }

    /** @brief funzione che verifica se il codice fiscale è valido.
        * @param codiceFiscale codice fiscale da controllare
        * @return restituisce True nel caso in cui il codice fiscale soddisfi il pattern  */
    fun isValidCodiceFiscale(codiceFiscale: String) : Boolean{
        val regex = Regex("^[a-zA-Z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}\$")
        return codiceFiscale.isNotEmpty() && regex.matches(codiceFiscale)
    }

    /** @brief funzione che verifica se il numero di telefono è valido.
    * @param numeroDiTelefono numero di telefono da controllare
    * @return restituisce True nel caso in cui il numero di telefono soddisfi il pattern  */
    fun isValidNumeroDiTelefono(numeroDiTelefono: String) : Boolean
    {
        val regex = Regex("^\\+?\\d{1,3}\\s?\\d{3,}\$")
        return numeroDiTelefono.isNotEmpty() && regex.matches(numeroDiTelefono)
    }

    /** @brief funzione che verifica se l'indirizzo è valido.
    * @param indirizzo indirizzo da controllare
    * @return restituisce True nel caso in cui l'indirizzo soddisfi il pattern
     * @sample Via Di Mezzo, 12, 65128, Pescara*/
    fun isValidIndirizzo(indirizzo : String): Boolean
    {
        val regex = Regex("^[A-Za-zÀ-ÿ\\\\s]+,\\\\s\\\\d{1,5},\\\\s\\\\d{5},\\\\s[A-Za-zÀ-ÿ\\\\s]+\$")
        return indirizzo.isNotEmpty() && regex.matches(indirizzo)
    }

    fun isFieldEmpty(text : String): Boolean{
        return text.isEmpty()
    }
}