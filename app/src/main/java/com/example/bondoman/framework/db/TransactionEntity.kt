package com.example.bondoman.framework.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bondoman.core.data.Transaction

@Entity(tableName = "transaction")
data class TransactionEntity(
    val title: String,
    val type: String,
    val nominal: Long,

    @ColumnInfo(name = "creation_time")
    val creationTime: Long,
    val location: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
) {
    companion object {
        fun fromTransaction(transaction: Transaction) = TransactionEntity(transaction.title,transaction.type, transaction.nominal, transaction.creationTime, transaction.location)
    }

    fun toTransaction() = Transaction(title, type, nominal, creationTime, location, id)
}
