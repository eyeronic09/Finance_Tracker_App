package com.example.financetracker.summaryScreen

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SummaryViewModel(private val transactionDao: TranscationDao) : ViewModel() {
    val allTransactions: StateFlow<List<Transaction>> = transactionDao.getAll()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val Summarys: StateFlow<List<Summary>> = allTransactions.map { transactions ->
        val categories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")
        categories.map { category ->
            val categoryTransactions = transactions.filter { it.category.equals(category, ignoreCase = true) }
            Log.d("categoryTransactions" , categoryTransactions.toString())
            val totalIncome = categoryTransactions.filter { it.type.equals("income", true) }.sumOf { it.amount }
            val expense = categoryTransactions.filter { it.type.equals("expense" ,true) }.sumOf { it.amount
            }
            Summary(
                category = category,
                income = totalIncome,
                expense = expense,
                netBalance = totalIncome - expense
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )



}