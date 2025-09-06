package com.example.financetracker.HomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao

/**
 * Factory for creating a [TranscationViewModel] with a constructor that takes a [TranscationDao].
 */
class TranscationViewModelFactory(
    private val transcationDao: TranscationDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TranscationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TranscationViewModel(transcationDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
