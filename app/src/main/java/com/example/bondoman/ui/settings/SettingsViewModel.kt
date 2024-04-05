package com.example.bondoman.ui.settings

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.core.repository.TransactionRepository
import com.example.bondoman.core.usecase.AddTransaction
import com.example.bondoman.core.usecase.GetAllTransaction
import com.example.bondoman.core.usecase.GetTransaction
import com.example.bondoman.core.usecase.GetTransactionTypeCount
import com.example.bondoman.core.usecase.RemoveTransaction
import com.example.bondoman.framework.RoomTransactionDataSource
import com.example.bondoman.framework.UseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val repository = TransactionRepository(RoomTransactionDataSource(application))

    val useCases = UseCases(
        AddTransaction(repository),
        GetTransaction(repository),
        GetAllTransaction(repository),
        RemoveTransaction(repository),
        GetTransactionTypeCount(repository),
    )

    val transactions = MutableLiveData<List<Transaction>>()

    fun getTransactions() {
        coroutineScope.launch {
            val transactionList = useCases.getAllTransaction()
            Log.d("Settings View Model", transactionList.toString())
            transactions.postValue(transactionList)
            Log.d("Settings View Model", transactions.value.toString())
        }
    }

    fun setTransactions(transactionList: List<Transaction>) {
        transactions.value = transactionList
    }
}