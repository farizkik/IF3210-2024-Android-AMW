package com.example.bondoman.core.repository.scan

import android.util.Log
import com.example.bondoman.api.APIClient
import com.example.bondoman.api.scan.dto.UploadResponse
import com.example.bondoman.common.response.ResponseContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response

class UploadRepository {
    suspend fun upload(token: String?, part: MultipartBody.Part): ResponseContract {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("Upload Repository", "Header: Bearer $token")
                val response: Response<UploadResponse> = APIClient.uploadService.upload("Bearer " + token!!, part).execute()

                if (response.isSuccessful) {
                    Log.d("Upload Repository", "Upload Succeeded")
                    ResponseContract.Success(response.body())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("Upload Repository", "Upload Failed: $errorBody")
                    ResponseContract.Error(errorBody!!)
                }
            } catch (e: Exception) {
                Log.e("Upload Repository", "Upload Failed: $e")
                ResponseContract.Error(e.toString())
            }
        }
    }
}