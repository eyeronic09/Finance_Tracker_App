package com.example.financetracker.Summary.SummaryModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import com.example.financetracker.Summary.SummaryData.Summary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class SummaryViewModel(private val transactionDao: TranscationDao) : ViewModel() {
    // State for date range selection

    private val _selectedDateRange = MutableStateFlow("Last 7 Days")
    val selectedDateRange: StateFlow<String?> = _selectedDateRange.asStateFlow()

    // State for custom date range
    private val _customStartDate = MutableStateFlow<LocalDate?>(null)
    val customStartDate: StateFlow<LocalDate?> = _customStartDate.asStateFlow()

    private val _customEndDate = MutableStateFlow<LocalDate?>(null)
    val customEndDate: StateFlow<LocalDate?> = _customEndDate.asStateFlow()

    // State for date validation errors
    private val _dateError = MutableStateFlow<String?>(null)
    val dateError: StateFlow<String?> = _dateError.asStateFlow()

    // Function to set custom date range
    fun setCustomDateRange(startDate: LocalDate, endDate: LocalDate) {
        _customStartDate.value = startDate
        _customEndDate.value = endDate
        _selectedDateRange.value = "Custom"
        _dateError.value = null
    }

    // Function to update selected date range
    fun selectedDateRange(range: String) {
        _selectedDateRange.value = range
        _dateError.value = null

        // Set default date ranges for predefined options
        val today = LocalDate.now()
        when (range) {
            "Last 7 Days" -> {
                _customStartDate.value = today.minusDays(6)
                _customEndDate.value = today
            }
            "Last 30 Days" -> {
                _customStartDate.value = today.minusDays(29)
                _customEndDate.value = today
            }
            "This Month" -> {
                _customStartDate.value = today.withDayOfMonth(1)
                _customEndDate.value = today.withDayOfMonth(today.lengthOfMonth())
            }
            else -> {
                return
            }
        }
    }



    // All transactions from Room database
    val allTransactions: StateFlow<List<Transaction>> = transactionDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered transactions by date range
    val filterDate: StateFlow<List<Transaction>> = combine(
        allTransactions,
        _customStartDate,
        _customEndDate,
        _selectedDateRange
    ) { transactions, start, end, range ->
        val today = LocalDate.now()
        val (startDate, endDate) = when (range) {
            "Last 7 Days" -> today.minusDays(6) to today
            "Last 30 Days" -> today.minusDays(29) to today
            "This Month" -> today.withDayOfMonth(1) to today.withDayOfMonth(today.lengthOfMonth())
            "Custom" -> if (start != null && end != null && !start.isAfter(end)) start to end else today.minusDays(29) to today
            else -> today.minusDays(29) to today
        }
        transactions.filter { transaction ->
            val transDate = transaction.date.toLocalDate()
            transDate.isAfter(startDate.minusDays(1)) && transDate.isBefore(endDate.plusDays(1))
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = emptyList(),
        )

    // Category summaries based on filtered transactions
    val summaryFlow: StateFlow<List<Summary>> = filterDate.map { transactions ->
        val categories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")
        categories.map { category ->
            val categoryTransactions = transactions.filter { it.category.equals(category, ignoreCase = true) }
            Log.d("categoryTransactions", "$category: $categoryTransactions")
            val totalIncome = categoryTransactions.filter { it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }
            val expense = categoryTransactions.filter { it.type.equals("expense", ignoreCase = true) }.sumOf { it.amount }
            Summary(
                category = category,
                income = totalIncome,
                expense = expense,
                netBalance = totalIncome - expense
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = emptyList()
    )

}