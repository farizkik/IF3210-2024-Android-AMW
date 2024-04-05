package com.example.bondoman.core.usecase

import com.example.bondoman.core.data.Transaction
import com.example.bondoman.core.repository.TransactionRepository

class RemoveTransaction(private val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) = transactionRepository.removeTransaction(transaction)
}