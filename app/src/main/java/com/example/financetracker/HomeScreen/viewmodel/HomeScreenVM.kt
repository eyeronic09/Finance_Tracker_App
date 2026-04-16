package com.example.financetracker.HomeScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeScreenUiState(
    val transactions: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction? = null,
    val todayDate: LocalDate,
    val isLoading: Boolean = false,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0
)

sealed interface HomeScreenEvent {

    data object LoadTransactions : HomeScreenEvent

    data class DeleteTransaction(val transaction: Transaction) : HomeScreenEvent

    data class SelectTransaction(val transaction: Transaction) : HomeScreenEvent

    data object ClearSelectedTransaction : HomeScreenEvent

    data class OnDateSelected(val date: LocalDate) : HomeScreenEvent

    data object ClearDateFilter : HomeScreenEvent
}

class HomeScreenViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeScreenUiState(
            todayDate = LocalDate.now()
        )
    )
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()


    fun onEvent(event: HomeScreenEvent) {
        when (event) {

            /* 🔹 Load Transactions */
            HomeScreenEvent.LoadTransactions -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }

                    val data = repository.getAllTransactions()
                    val income = data.filter { it.type.equals("Income", ignoreCase = true) }.sumOf { it.amount }
                    val expense = data.filter { it.type.equals("Expense", ignoreCase = true) }.sumOf { it.amount }

                    _uiState.update {
                        it.copy(
                            transactions = data,
                            isLoading = false,
                            totalIncome = income,
                            totalExpense = expense,
                            balance = income - expense
                        )
                    }
                }
            }

            /* 🔹 Delete Transaction */
            is HomeScreenEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    repository.deleteTransaction(event.transaction)

                    val updated = repository.getAllTransactions()
                    val income = updated.filter { it.type.equals("Income", ignoreCase = true) }.sumOf { it.amount }
                    val expense = updated.filter { it.type.equals("Expense", ignoreCase = true) }.sumOf { it.amount }

                    _uiState.update {
                        it.copy(
                            transactions = updated,
                            totalIncome = income,
                            totalExpense = expense,
                            balance = income - expense
                        )
                    }
                }
            }

            /* 🔹 Select Transaction */
            is HomeScreenEvent.SelectTransaction -> {
                _uiState.update {
                    it.copy(selectedTransaction = event.transaction)
                }
            }

            /* 🔹 Clear Selected */
            HomeScreenEvent.ClearSelectedTransaction -> {
                _uiState.update {
                    it.copy(selectedTransaction = null)
                }
            }


            is HomeScreenEvent.OnDateSelected -> {
                viewModelScope.launch {
                    val all = repository.getAllTransactions()

                    val filtered = all.filter {
                        it.date.toLocalDate() == event.date
                    }

                    _uiState.update {
                        it.copy(
                            todayDate = event.date,
                            transactions = filtered
                        )
                    }
                }
            }

            /* 🔹 Clear Date Filter */
            HomeScreenEvent.ClearDateFilter -> {
                viewModelScope.launch {
                    val all = repository.getAllTransactions()

                    _uiState.update {
                        it.copy(

                            transactions = all
                        )
                    }
                }
            }

        }
    }
    init {
        loadTranscation()
    }
    fun loadTranscation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val data = repository.getAllTransactions()
            val income = data.filter { it.type.equals("Income", ignoreCase = true) }.sumOf { it.amount }
            val expense = data.filter { it.type.equals("Expense", ignoreCase = true) }.sumOf { it.amount }

            _uiState.update {
                it.copy(
                    transactions = data,
                    isLoading = false,
                    totalIncome = income,
                    totalExpense = expense,
                    balance = income - expense
                )
            }
        }
    }
}