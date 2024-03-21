package com.example.bondoman.core.repository

import com.example.bondoman.core.data.Transaction
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE

class TransactionRepository(private val dataSource: TransactionDataSource) {
    suspend fun  addTransaction(transaction: Transaction) = dataSource.add(transaction)

    suspend fun getTransaction(id: Long) = dataSource.get(id)

    suspend fun getAllTransactions() = dataSource.getAll()

    suspend fun getTransactionTypeCount(type: TRANSACTION_TYPE) = dataSource.getTransactionTypeCount(type)

    suspend fun removeTransaction(transaction: Transaction) = dataSource.remove(transaction)
}