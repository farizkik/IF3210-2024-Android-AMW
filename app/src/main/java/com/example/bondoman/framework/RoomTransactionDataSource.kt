package com.example.bondoman.framework

import android.content.Context
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.core.repository.TransactionDataSource
import com.example.bondoman.framework.db.DatabaseService
import com.example.bondoman.framework.db.TransactionEntity

class RoomTransactionDataSource(context: Context) : TransactionDataSource {
    val transactionDao = DatabaseService.getInstance(context).transactionDao()

    override suspend fun add(transaction: Transaction) = transactionDao.addTransactionEntity(
        TransactionEntity.fromTransaction(transaction)
    )

    override suspend fun get(id: Long) = transactionDao.getTransactionEntity(id)?.toTransaction()


    override suspend fun getAll() = transactionDao.getAllTransactionEntities().map{it.toTransaction()}

    override suspend fun remove(transaction: Transaction) = transactionDao.deleteTransactionEntity(
        TransactionEntity.fromTransaction(transaction))
}