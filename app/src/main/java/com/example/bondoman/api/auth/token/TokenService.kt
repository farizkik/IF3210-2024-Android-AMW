package com.example.bondoman.api.auth.token

import com.example.bondoman.api.auth.token.dto.TokenResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenService {
    @POST("api/auth/token")
    fun getToken (
        @Header("Authorization") token: String?
    ) : Call<TokenResponse>
}