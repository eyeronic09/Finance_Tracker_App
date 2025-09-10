package com.example.financetracker.Summary.SummaryModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TransactionChartViewModel(private val transactionDao: TranscationDao): ViewModel() {
    val allTransaction: StateFlow<List<Transaction>> = transactionDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(3000),
        initialValue = emptyList()
    )
/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
    val categoryTotalAndLable: StateFlow<Map<String, Double>> = allTransaction.map { transactions ->
        transactions.groupBy { it.category }
            .mapValues { (_, sumAmount) ->
                sumAmount.sumOf { if (it.type.equals("expense", ignoreCase = true)) it.amount else -it.amount }
            }
        val grouped = transactions.groupBy { it.category }
        Log.d("CategoryDebug", "Grouped into ${grouped.size} categories: ${grouped.keys}")

        grouped.mapValues { (_, sumAmount) ->
            sumAmount.sumOf { if (it.type.equals("income" , ignoreCase = true) ){it.amount} else -it.amount }
        }

    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = emptyMap()
    )

}
