package com.example.bondoman.framework

import com.example.bondoman.core.usecase.AddTransaction
import com.example.bondoman.core.usecase.GetAllTransaction
import com.example.bondoman.core.usecase.GetTransaction
import com.example.bondoman.core.usecase.RemoveTransaction

data class UseCases(
    val addTransaction: AddTransaction,
    val getTransaction: GetTransaction,
    val getAllTransaction: GetAllTransaction,
    val removeTransaction: RemoveTransaction,
)
