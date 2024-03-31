package com.example.bondoman.core.repository.auth.token

import android.util.Log
import com.example.bondoman.api.APIClient
import com.example.bondoman.api.auth.token.dto.TokenResponse
import com.example.bondoman.common.response.ResponseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TokenRepository {

    suspend fun getToken(headerAuth: String?): ResponseContract{
        return withContext(Dispatchers.IO) {
            try {
                Log.d("Token Repository", "This is header $headerAuth")
                val response: Response<TokenResponse> = APIClient.tokenService.getToken(headerAuth).execute()
                if (response.isSuccessful) {
                    ResponseContract.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("Token Repository", "This is message: $errorBody")
                    ResponseContract.Error(errorBody!!)
                }
            } catch (e: Exception) {
                Log.e("Token Repository", "Failed to get token", e)
                ResponseContract.Error(e.toString())
            }
        }
    }
}