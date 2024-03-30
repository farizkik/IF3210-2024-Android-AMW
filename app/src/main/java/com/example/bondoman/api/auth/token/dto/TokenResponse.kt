package com.example.bondoman.api.auth.token.dto

import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "nim")
    val nim: String,

    @Json(name="iat")
    val iat: Long,

    @Json(name="exp")
    val exp: Long
)
