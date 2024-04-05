package com.example.bondoman.core.data

import android.os.Parcel
import android.os.Parcelable

data class ParcelableItem(
    val name: String,
    val qty: Int,
    val price: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(qty)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableItem> {
        override fun createFromParcel(parcel: Parcel): ParcelableItem {
            return ParcelableItem(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableItem?> {
            return arrayOfNulls(size)
        }
    }
}