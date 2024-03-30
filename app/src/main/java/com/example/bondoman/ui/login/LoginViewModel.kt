package com.example.bondoman.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.api.auth.login.dto.LoginResponse
import com.example.bondoman.common.response.ResponseContract
import com.example.bondoman.core.repository.auth.login.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application):AndroidViewModel(application) {
    private val _loginResponse = MutableLiveData<LoginResponse>()

    val loginResponse:LiveData<LoginResponse> = _loginResponse

    private val loginRepository = LoginRepository()

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage:LiveData<String> = _errorMessage


    fun login(req: LoginRequest) {
        viewModelScope.launch {

            when (val result = loginRepository.login(req)) {
                is ResponseContract.Success<*> -> {
                    _loginResponse.value = (result.response as LoginResponse?)
                }

                is ResponseContract.Error -> {
                    _errorMessage.value = result.response
                }
            }
        }
    }
}