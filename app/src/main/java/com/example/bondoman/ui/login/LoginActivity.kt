package com.example.bondoman.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.MainActivity
import com.example.bondoman.R
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.databinding.ActivityLoginBinding
import com.example.bondoman.service.ConnectivityObserver
import com.example.bondoman.service.NetworkConnectivityObserver
import com.example.bondoman.share_preference.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferenceManager: PreferenceManager

    private lateinit var connectivityObserver: ConnectivityObserver
    private var alertDialog: AlertDialog? = null

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        preferenceManager = PreferenceManager(this)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

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

            if(!connectivityObserver.isConnected()) {
                Toast.makeText(this@LoginActivity, "Please connect to a network", Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            observeConnectivity()
        }
    }

    private fun observeConnectivity() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost) {
                    showNoInternetPopUp()
                } else {
                    hideNoInternetPopup()
                }
            }
        }
    }

    private fun showNoInternetPopUp() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun hideNoInternetPopup() {
        alertDialog?.dismiss()
        alertDialog = null
    }

}