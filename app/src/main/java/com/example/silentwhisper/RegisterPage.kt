package com.example.silentwhisper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.silentwhisper.databinding.ActivityRegisterPageBinding

lateinit var bind:ActivityRegisterPageBinding
class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind=ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(bind.root)

    }
}