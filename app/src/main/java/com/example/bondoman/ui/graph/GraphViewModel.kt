package com.example.bondoman.ui.graph

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.bondoman.core.repository.TransactionRepository
import com.example.bondoman.core.usecase.AddTransaction
import com.example.bondoman.core.usecase.GetAllTransaction
import com.example.bondoman.core.usecase.GetTransaction
import com.example.bondoman.core.usecase.GetTransactionTypeCount
import com.example.bondoman.core.usecase.RemoveTransaction
import com.example.bondoman.framework.RoomTransactionDataSource
import com.example.bondoman.framework.UseCases
import com.example.bondoman.lib.transaction.TRANSACTION_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraphViewModel(application: Application):AndroidViewModel(application) {

    private val _countData = MutableLiveData<List<DataEntry>>()

    val countData: LiveData<List<DataEntry>> = _countData

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    val repository = TransactionRepository(RoomTransactionDataSource(application))

    val useCases = UseCases(
        AddTransaction(repository),
        GetTransaction(repository),
        GetAllTransaction(repository),
        RemoveTransaction(repository),
        GetTransactionTypeCount(repository)
    )

    fun getCountTypeData(){
        coroutineScope.launch {
            val pemasukanCount:Int = useCases.getTransactionTypeCount(TRANSACTION_TYPE.PEMASUKAN)
            val pembelianCount:Int = useCases.getTransactionTypeCount(TRANSACTION_TYPE.PEMBELIAN)

            val dataEntries:List<DataEntry> = listOf(
                    ValueDataEntry("Pemasukan", pemasukanCount),
                    ValueDataEntry("Pembelian", pembelianCount))

            withContext(Dispatchers.Main) {
                _countData.value = dataEntries
            }
        }
    }
}