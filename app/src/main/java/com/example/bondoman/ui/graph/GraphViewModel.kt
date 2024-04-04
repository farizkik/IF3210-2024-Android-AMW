package com.example.bondoman.ui.graph

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bondoman.core.repository.TransactionRepository
import com.example.bondoman.core.usecase.AddTransaction
import com.example.bondoman.core.usecase.GetAllTransaction
import com.example.bondoman.core.usecase.GetTransaction
import com.example.bondoman.core.usecase.GetTransactionTypeCount
import com.example.bondoman.core.usecase.RemoveTransaction
import com.example.bondoman.framework.RoomTransactionDataSource
import com.example.bondoman.framework.UseCases
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GraphViewModel(application: Application):AndroidViewModel(application) {

    val repository = TransactionRepository(RoomTransactionDataSource(application))

    val useCases = UseCases(
        AddTransaction(repository),
        GetTransaction(repository),
        GetAllTransaction(repository),
        RemoveTransaction(repository),
        GetTransactionTypeCount(repository)
    )

    suspend fun getCountTypeData(): Pair<Int, Int>{
        return withContext(Dispatchers.Default) {
            val pemasukanCount = useCases.getTransactionTypeCount(TRANSACTION_TYPE.PEMASUKAN)
            val pembelianCount = useCases.getTransactionTypeCount(TRANSACTION_TYPE.PEMBELIAN)
            Pair(pemasukanCount, pembelianCount)
        }
    }
}