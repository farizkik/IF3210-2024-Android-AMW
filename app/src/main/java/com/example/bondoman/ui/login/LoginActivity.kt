package com.example.bondoman.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.MainActivity
import com.example.bondoman.R
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.share_preference.PreferenceManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferenceManager: PreferenceManager

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

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
                binding.editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            } else if (!email.matches(emailPattern)) {
                binding.editTextTextEmailAddress.error = "Invalid email format"
                binding.editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty() || password == "") {
                binding.editTextTextPassword.error = "Password is required"
                binding.editTextTextPassword.requestFocus()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)

            Log.d("Login", loginRequest.email.toString())
            Log.d("Login", loginRequest.password.toString())
            viewModel.login(loginRequest)
        }


        viewModel.loginResponse.observe(this, Observer { res ->
            Log.d("Login", res.toString())
            preferenceManager.setToken(res.token)

            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        })

        viewModel.errorMessage.observe(this, Observer { res ->
            Log.d("Login", res.toString())
            if (res.toString() == "Invalid email") {
                binding.editTextTextEmailAddress.error = res.toString()
                binding.editTextTextPassword.setText("")
                binding.editTextTextEmailAddress.requestFocus()
                binding.editTextTextPassword.error = null
            }

            if (res.toString() == "Invalid password") {
                binding.editTextTextPassword.error = res.toString()
                binding.editTextTextPassword.requestFocus()
            }
        })


    }

}