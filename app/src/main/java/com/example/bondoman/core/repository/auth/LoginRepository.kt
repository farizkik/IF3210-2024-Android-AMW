package com.example.bondoman.core.repository.auth

import android.util.Log
import com.example.bondoman.api.auth.APIClient
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.api.auth.login.dto.LoginResponse
import com.example.bondoman.lib.error.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {
    suspend fun login(req: LoginRequest) : LoginResult {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<LoginResponse> = APIClient.loginService.login(req).execute()
                if (response.isSuccessful) {
                    LoginResult.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val adapter = APIClient.moshi.adapter(ErrorResponse::class.java)
                    val errorResponse = errorBody?.let { adapter.fromJson(it) }
                    LoginResult.Error(errorResponse.toString())
                }
            } catch (e: Exception) {
                Log.e("Login Repository", "Login Failed", e)
                LoginResult.Error(e.toString())
            }
        }

    }
}
