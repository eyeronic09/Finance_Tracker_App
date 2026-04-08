package com.example.financetracker.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*


data class HomeScreenUiState(
    val transactions: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction? = null,
    val selectedDate: LocalDate? = null,
    val todayDate: LocalDate,
    val isLoading: Boolean = false
)
sealed interface HomeScreenEvent {

    data object LoadTransactions : HomeScreenEvent

    data class DeleteTransaction(val transaction: Transaction) : HomeScreenEvent

    data class SelectTransaction(val transaction: Transaction) : HomeScreenEvent

    data object ClearSelectedTransaction : HomeScreenEvent

    data class OnDateSelected(val date: LocalDate) : HomeScreenEvent

    data object ClearDateFilter : HomeScreenEvent

    data object NavigateToAddTransaction : HomeScreenEvent
}

/* ----------------------------- VIEWMODEL ----------------------------- */

class HomeScreenViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeScreenUiState(
            todayDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        )
    )
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    init {
        onEvent(HomeScreenEvent.LoadTransactions)
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {

            /* 🔹 Load Transactions */
            HomeScreenEvent.LoadTransactions -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }

                    val data = repository.getAllTransactions()

                    _uiState.update {
                        it.copy(
                            transactions = data,
                            isLoading = false
                        )
                    }
                }
            }

            /* 🔹 Delete Transaction */
            is HomeScreenEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    repository.deleteTransaction(event.transaction)

                    val updated = repository.getAllTransactions()

                    _uiState.update {
                        it.copy(transactions = updated)
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

            /* 🔹 Filter by Date */
            is HomeScreenEvent.OnDateSelected -> {
                viewModelScope.launch {
                    val all = repository.getAllTransactions()

                    val filtered = all.filter {
                        Instant.fromEpochMilliseconds(it.date)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date == event.date
                    }

                    _uiState.update {
                        it.copy(
                            selectedDate = event.date,
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
                            selectedDate = null,
                            transactions = all
                        )
                    }
                }
            }

            HomeScreenEvent.NavigateToAddTransaction -> {
                // UI handles navigation (Navigator)
            }
        }
    }
}