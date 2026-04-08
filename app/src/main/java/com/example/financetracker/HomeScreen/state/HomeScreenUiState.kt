package com.example.financetracker.HomeScreen.state

import com.example.financetracker.core.domain.model.Transaction
import kotlinx.datetime.LocalDate

/**
 * UI State for the Home Screen
 */
data class HomeScreenUiState(
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val todayDate: LocalDate,
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = false,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0,
    val selectedTransaction: Transaction? = null
)