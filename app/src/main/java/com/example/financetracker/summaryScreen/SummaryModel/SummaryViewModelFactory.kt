package com.example.financetracker.summaryScreen.SummaryModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import com.example.financetracker.summaryScreen.SummaryViewModel

class SummaryViewModelFactory(
    private val transactionDao: TranscationDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummaryViewModel::class.java)) {
            return SummaryViewModel(transactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}