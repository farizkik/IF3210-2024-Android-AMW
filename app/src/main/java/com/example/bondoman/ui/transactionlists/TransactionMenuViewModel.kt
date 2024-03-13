package com.example.bondoman.ui.transactionlists

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bondoman.core.data.Transaction
import com.example.bondoman.core.repository.TransactionRepository
import com.example.bondoman.core.usecase.AddTransaction
import com.example.bondoman.core.usecase.GetAllTransaction
import com.example.bondoman.core.usecase.GetTransaction
import com.example.bondoman.core.usecase.RemoveTransaction
import com.example.bondoman.framework.RoomTransactionDataSource
import com.example.bondoman.framework.UseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val repository = TransactionRepository(RoomTransactionDataSource(application))

    val useCases = UseCases(
        AddTransaction(repository),
        GetTransaction(repository),
        GetAllTransaction(repository),
        RemoveTransaction(repository)
    )

    val transactions = MutableLiveData<List<Transaction>>()

    fun getTransactions(){
        coroutineScope.launch {
            val transactionList = useCases.getAllTransaction()
            transactions.postValue(transactionList)
        }
    }
}