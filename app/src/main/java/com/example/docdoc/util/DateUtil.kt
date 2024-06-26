package com.example.docdoc.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object{

        /** @brief funzione che restituisce la data nel formato YYYY-MM-DD
         * @return String nel formato YYYY-MM-DD
         */
        fun getCurrentDate(): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return LocalDate.now().format(formatter)
        }
    }
}