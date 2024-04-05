package com.example.bondoman.core.usecase

import com.example.bondoman.core.repository.TransactionRepository
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE

class GetTransactionTypeCount(private val transactionRepository: TransactionRepository) {
    suspend operator fun invoke (transactionType: TRANSACTION_TYPE) = transactionRepository.getTransactionTypeCount(transactionType)
}