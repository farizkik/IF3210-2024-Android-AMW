package com.example.bondoman.common.response

sealed class ResponseContract {
    data class Success<T>(val response: T) : ResponseContract()
    data class Error(val response: String) : ResponseContract()
}