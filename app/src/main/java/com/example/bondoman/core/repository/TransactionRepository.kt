package com.example.bondoman.core.repository

import com.example.bondoman.core.data.Transaction

class TransactionRepository(private val dataSource: TransactionDataSource) {
    suspend fun  addTransaction(transaction: Transaction) = dataSource.add(transaction)

    suspend fun getTransaction(id: Long) = dataSource.get(id)

    suspend fun getAllTransactions() = dataSource.getAll()

    suspend fun removeTransaction(transaction: Transaction) = dataSource.remove(transaction)
}