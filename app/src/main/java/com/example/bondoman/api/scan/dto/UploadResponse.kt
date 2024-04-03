package com.example.bondoman.api.scan.dto

import com.example.bondoman.core.data.Items
import com.squareup.moshi.Json

data class UploadResponse(
    @Json(name = "items")
    val items: Items
)