package com.example.bondoman.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.api.scan.dto.UploadResponse
import com.example.bondoman.common.response.ResponseContract
import com.example.bondoman.core.data.Items
import com.example.bondoman.core.repository.scan.UploadRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UploadViewModel : ViewModel() {

    private val uploadRepository = UploadRepository()

    private val _uploadResponse = MutableLiveData<UploadResponse>()

    val uploadResponse: LiveData<UploadResponse> = _uploadResponse

    private val _errorMessage = MutableLiveData<String>()

    val errorMessage: LiveData<String> = _errorMessage

    private val _items = MutableLiveData<Items>()

    val items: LiveData<Items> = _items

    fun upload(token: String?, part: MultipartBody.Part) {
        viewModelScope.launch {
            when (val result = uploadRepository.upload(token, part)) {
                is ResponseContract.Success<*> -> {
                    _uploadResponse.value = (result.response as UploadResponse)
                    _items.value = _uploadResponse.value!!.items
                    Log.d("Upload View Model", items.value.toString())
                }

                is ResponseContract.Error -> {
                    _errorMessage.value = result.response
                    Log.d("Upload View Model", _errorMessage.value.toString())
                }
            }
        }
    }

    fun setItems(res: UploadResponse) {
        _items.value = res.items
    }
}