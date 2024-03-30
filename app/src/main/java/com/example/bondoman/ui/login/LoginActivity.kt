package com.example.bondoman.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.R
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.share_preference.PreferenceManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        preferenceManager = PreferenceManager(this)



        binding.button2.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            if(email.isEmpty() || email == "") {
                binding.editTextTextEmailAddress.error = "Email is required"
                return@setOnClickListener
            }

            if(password.isEmpty() || password == "") {
                binding.editTextTextPassword.error = "Password is required"
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)

            Log.d("Login", loginRequest.email.toString())
            Log.d("Login", loginRequest.password.toString())
            viewModel.login(loginRequest)
        }


        viewModel.loginResponse.observe(this, Observer { res ->
            Log.d("Login", res.toString())
        })

        viewModel.errorMessage.observe(this, Observer { res ->
            Log.d("Login", res.toString())
        })


    }

}