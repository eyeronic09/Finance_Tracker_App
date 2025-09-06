package com.example.financetracker.summaryScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
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
    private val _selectedDateRange = MutableStateFlow("last7") // Default to last 7 days
    val selectedDateRange: StateFlow<String> = _selectedDateRange.asStateFlow()

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
        _selectedDateRange.value = "custom"
        _dateError.value = null
    }
    
    // Function to update selected date range
    fun selectedDateRange(range: String) {
        _selectedDateRange.value = range
        _dateError.value = null
        
        // Set default date ranges for predefined options
        val today = LocalDate.now()
        when (range) {
            "last7" -> {
                _customStartDate.value = today.minusDays(6)
                _customEndDate.value = today
            }
            "last30" -> {
                _customStartDate.value = today.minusDays(29)
                _customEndDate.value = today
            }
            // For "custom", the dates should be set via setCustomDateRange
        }
    }
    
    // Initialize with default date range
    init {
        selectedDateRange("last7")
    }

    // All transactions from Room database
    val allTransactions: StateFlow<List<Transaction>> = transactionDao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
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
            "last7" -> today.minusDays(1) to today
            "last30" -> today.minusDays(30) to today
            "custom" -> if (start != null && end != null && !start.isAfter(end)) {
                start to end
            } else {
                today.minusDays(30) to today // Fallback
            }
            else -> today.minusDays(30) to today // Default
        }
        transactions.filter { transaction ->
            val transDate = transaction.date.toLocalDate()
            Log.d("transctionsLast7" , transactions.toString())
            transDate.isAfter(startDate.minusDays(1)) && transDate.isBefore(endDate.plusDays(1))}

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

}