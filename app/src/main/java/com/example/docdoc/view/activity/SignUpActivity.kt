package com.example.docdoc.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.docdoc.databinding.ActivityRegistratiBinding

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        val binding = ActivityRegistratiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}