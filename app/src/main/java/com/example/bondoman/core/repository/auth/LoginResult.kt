package com.example.bondoman.core.repository.auth

import com.example.bondoman.api.auth.login.dto.LoginResponse

sealed class LoginResult {
    data class Success(val response: LoginResponse) : LoginResult()
    data class Error(val response: String) : LoginResult()
}