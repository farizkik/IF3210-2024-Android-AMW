package com.example.bondoman.core.data

data class Transaction (
    var title: String,
    var type: String,
    var nominal: Long,
    var creationTime: Long,
    var location: String,
    var latitude: Double,
    var longitude: Double,
    var id: Long = 0
)
