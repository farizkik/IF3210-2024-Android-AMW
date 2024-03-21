package com.example.bondoman.framework.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE

@Dao

interface TransactionDao {
    @Insert(onConflict = 1)
     fun addTransactionEntity(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM `transaction` WHERE id = :id")
     fun getTransactionEntity(id: Long): TransactionEntity?

    @Query("SELECT * FROM `transaction`")
     fun getAllTransactionEntities(): List<TransactionEntity>

     @Query("SELECT COUNT(*) FROM `transaction` WHERE type = :type")
     fun getTransactionTypeCount(type: TRANSACTION_TYPE): Int

    @Delete
     fun deleteTransactionEntity(transactionEntity: TransactionEntity)
}