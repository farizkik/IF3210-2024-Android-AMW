package com.example.bondoman.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.auth.APIClient
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.api.auth.login.dto.LoginResponse
import com.example.bondoman.core.repository.auth.LoginRepository
import com.example.bondoman.core.repository.auth.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application):AndroidViewModel(application) {
    private val _loginResponse = MutableLiveData<LoginResponse>()

    val loginResponse:LiveData<LoginResponse> = _loginResponse

    private val loginRepository = LoginRepository()

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage:LiveData<String> = _errorMessage


    fun login(req: LoginRequest) {
        viewModelScope.launch {

            when (val result = loginRepository.login(req)) {
                is LoginResult.Success -> {
                    _loginResponse.value = result.response
                }

                is LoginResult.Error -> {
                    _errorMessage.value = result.response
                }
            }
        }
    }
}