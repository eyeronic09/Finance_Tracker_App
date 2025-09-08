package com.example.financetracker.Summary.SummaryModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao

class TransactionChartViewModelFactory(
    private val transactionDao: TranscationDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionChartViewModel::class.java)) {
            return TransactionChartViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}