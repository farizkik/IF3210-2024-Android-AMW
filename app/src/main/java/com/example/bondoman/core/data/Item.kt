package com.example.bondoman.core.data

data class Item(
    val name: String,
    val qty: Int,
    val price: Float
){
    constructor(parcelableItem: ParcelableItem) : this(
        parcelableItem.name,
        parcelableItem.qty,
        parcelableItem.price
    )
}

