package com.example.docdoc.util

class EventoUtil {
    companion object{
        private var currentId: Int = 0

        @Synchronized
        fun generateId(): String {
            return "EID_${currentId++}"
        }
    }
}