package com.example.bondoman.api.auth.login.dto

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name="token")
    val token: String
)
