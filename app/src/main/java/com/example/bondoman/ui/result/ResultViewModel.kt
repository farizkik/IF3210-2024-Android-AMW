package com.example.bondoman.ui.result

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application){
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val repository = TransactionRepository(RoomTransactionDataSource(application))

    val useCases = UseCases(
        AddTransaction(repository),
        GetTransaction(repository),
        GetAllTransaction(repository),
        RemoveTransaction(repository),
        GetTransactionTypeCount(repository)
    )

    var saved = MutableLiveData<Boolean>()
    val currentTransaction = MutableLiveData<Transaction?>()

    fun saveTransaction(transaction: Transaction) {
        coroutineScope.launch {
            useCases.addTransaction(transaction)
            Log.d("chane", "Observer triggered with yippie")
            saved.postValue(true)
        }
    }

    fun getTransaction(id: Long){
        coroutineScope.launch {
            val transaction = useCases.getTransaction(id)
            currentTransaction.postValue(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction){
        coroutineScope.launch {
            useCases.removeTransaction(transaction)
            saved.postValue(true)
        }
    }
}