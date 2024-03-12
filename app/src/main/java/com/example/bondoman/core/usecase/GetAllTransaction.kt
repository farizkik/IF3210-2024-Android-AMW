package com.example.bondoman.core.usecase

import com.example.bondoman.core.repository.TransactionRepository

class GetTransaction (private val transactionRepository: TransactionRepository){
    suspend operator fun invoke(id:Long) = transactionRepository.getTransaction(id)
}