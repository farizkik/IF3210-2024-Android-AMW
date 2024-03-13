package com.example.bondoman.core.usecase

import com.example.bondoman.core.repository.TransactionRepository

class GetAllTransaction (private val transactionRepository: TransactionRepository){
    suspend operator fun invoke() = transactionRepository.getAllTransactions()
}