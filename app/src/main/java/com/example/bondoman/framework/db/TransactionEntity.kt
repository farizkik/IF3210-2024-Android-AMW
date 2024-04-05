package com.example.bondoman.framework.db

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE

@Entity(tableName = "transaction")
data class TransactionEntity(
    val title: String,
    val type: TRANSACTION_TYPE,
    val nominal: Long,

    @ColumnInfo(name = "creation_time")
    val creationTime: Long,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
) {
    companion object {
        fun fromTransaction(transaction: Transaction): TransactionEntity {
            val entity = TransactionEntity(
                title = transaction.title,
                type = TRANSACTION_TYPE.valueOf(transaction.type),
                nominal = transaction.nominal,
                creationTime = transaction.creationTime,
                location = transaction.location,
                latitude = transaction.latitude,
                longitude = transaction.longitude,
                id = transaction.id
            )

            // Log the data before returning the entity
            Log.d("TransactionEntity", "Converted Transaction to TransactionEntity: $transaction -> $entity")

            return entity
        }
    }

    fun toTransaction() = Transaction(title, type.name, nominal, creationTime, location, latitude, longitude, id)
}
