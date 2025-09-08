package com.example.financetracker.Summary.SummaryModel

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
    val categoryTotalAndLable: StateFlow<Map<String, Double>> = allTransaction.map { transactions ->
        transactions.groupBy { it.category }
            .mapValues { (category, sumAmount) ->
                sumAmount.sumOf { it.amount }
            }

    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )


}