package com.example.bondoman.api.auth.login

import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.api.auth.login.dto.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("api/auth/login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>
}