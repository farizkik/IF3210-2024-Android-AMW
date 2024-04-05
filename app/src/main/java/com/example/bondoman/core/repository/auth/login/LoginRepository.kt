package com.example.bondoman.core.repository.auth.login

import android.util.Log
import com.example.bondoman.api.APIClient
import com.example.bondoman.api.auth.login.dto.LoginRequest
import com.example.bondoman.api.auth.login.dto.LoginResponse
import com.example.bondoman.common.response.ResponseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {
    suspend fun login(req: LoginRequest) : ResponseContract {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<LoginResponse> = APIClient.loginService.login(req).execute()
                if (response.isSuccessful) {
                    ResponseContract.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("Login Repository", "This is message: $errorBody")
                    ResponseContract.Error(errorBody!!)
                }
            } catch (e: Exception) {
                Log.e("Login Repository", "Login Failed", e)
                ResponseContract.Error(e.toString())
            }
        }

    }
}
