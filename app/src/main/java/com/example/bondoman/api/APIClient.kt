package com.example.bondoman.api.auth

import com.example.bondoman.api.auth.login.LoginService
import com.example.bondoman.common.Constant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object APIClient {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit: Retrofit by lazy { Retrofit.Builder().baseUrl(Constant.BASE_URL).addConverterFactory(
        MoshiConverterFactory.create(moshi)).build() }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }
}