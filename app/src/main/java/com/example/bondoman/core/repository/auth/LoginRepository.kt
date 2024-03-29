package com.example.bondoman.core.repository.auth

import com.example.bondoman.api.auth.APIClient
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.api.auth.login.dto.LoginResponse
import retrofit2.Response

class LoginRepository {
    suspend fun login(req: LoginRequest): Response<LoginResponse> {
        return APIClient.loginService.login(req)
    }
}