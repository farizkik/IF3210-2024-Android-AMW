package com.example.bondoman.core.repository

import com.example.bondoman.core.data.Transaction
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE


interface TransactionDataSource {
    suspend fun add(transaction: Transaction)

    suspend fun get(id:Long): Transaction?

    suspend fun getAll(): List<Transaction>

    suspend fun getTransactionTypeCount(type: TRANSACTION_TYPE): Int

    suspend fun remove(transaction: Transaction)
}