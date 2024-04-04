package com.example.bondoman.framework

import android.content.Context
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.core.repository.TransactionDataSource
import com.example.bondoman.framework.db.DatabaseService
import com.example.bondoman.framework.db.TransactionEntity
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomTransactionDataSource(context: Context) : TransactionDataSource {
    val transactionDao = DatabaseService.getInstance(context).transactionDao()
    init {
        CoroutineScope(Dispatchers.IO).launch {
        }
    }

    private suspend fun seedDatabase(context: Context) {
        if (transactionDao.getAllTransactionEntities().isEmpty()) {
            val transactions = listOf(
                TransactionEntity("Transaction 1", TRANSACTION_TYPE.PEMASUKAN, 100, System.currentTimeMillis(), "Location 1",0.0,0.0, ),
                TransactionEntity("Transaction 2", TRANSACTION_TYPE.PEMBELIAN, 200, System.currentTimeMillis(), "Location 2", 0.0 , 0.0)
            )

            transactions.forEach { transaction ->
                transactionDao.addTransactionEntity(transaction)
            }
        }
    }


    override suspend fun add(transaction: Transaction) = transactionDao.addTransactionEntity(
        TransactionEntity.fromTransaction(transaction)
    )

    override suspend fun get(id: Long) = transactionDao.getTransactionEntity(id)?.toTransaction()


    override suspend fun getAll() = transactionDao.getAllTransactionEntities().map{it.toTransaction()}

    override suspend fun getTransactionTypeCount(type: TRANSACTION_TYPE) = transactionDao.getTransactionTypeCount(type)

    override suspend fun remove(transaction: Transaction) = transactionDao.deleteTransactionEntity(
        TransactionEntity.fromTransaction(transaction))
}