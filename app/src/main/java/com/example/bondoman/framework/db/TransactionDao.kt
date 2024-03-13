package com.example.bondoman.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao

interface TransactionDao {
    @Insert(onConflict = 1)
     fun addTransactionEntity(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM `transaction` WHERE id = :id")
     fun getTransactionEntity(id: Long): TransactionEntity?

    @Query("SELECT * FROM `transaction`")
     fun getAllTransactionEntities(): List<TransactionEntity>

    @Delete
     fun deleteTransactionEntity(transactionEntity: TransactionEntity)
}