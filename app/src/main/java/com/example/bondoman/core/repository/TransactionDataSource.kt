package com.example.bondoman.core.repository

import com.example.bondoman.core.data.Transaction


interface TransactionDataSource {
    suspend fun add(transaction: Transaction)

    suspend fun get(id:Long): Transaction?

    suspend fun getAll(): List<Transaction>

    suspend fun remove(transaction: Transaction)
}