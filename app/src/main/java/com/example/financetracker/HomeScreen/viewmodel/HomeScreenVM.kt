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
    val openDatePicker : Boolean = false,
    val selectedTransaction: Transaction? = null,
    val todayDate: LocalDate,
    val isLoading: Boolean = false,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0
)

sealed interface HomeScreenEvent {
        data class DeleteTransaction(val transaction: Transaction) : HomeScreenEvent

    data class SelectTransaction(val transaction: Transaction) : HomeScreenEvent

    data object ClearSelectedTransaction : HomeScreenEvent

    data class OnDateSelected(val date: LocalDate) : HomeScreenEvent

    data object OpenDatePicker : HomeScreenEvent
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
            is HomeScreenEvent.DeleteTransaction -> {

            }

            is HomeScreenEvent.SelectTransaction -> {
                _uiState.update {
                    it.copy(selectedTransaction = event.transaction)
                }
            }

            HomeScreenEvent.ClearSelectedTransaction -> {
                _uiState.update {
                    it.copy(selectedTransaction = null)
                }
            }


            is HomeScreenEvent.OnDateSelected -> {
                viewModelScope.launch {
                    val all = repository.getAllTransactions()

                    val filtered = all.filter { it.date.toLocalDate() == event.date }
                    val income =  filtered.filter { it.type.equals("income", ignoreCase = true) }
                    val expense = filtered.filter {it.type.equals("expenses"  , true)}


                    _uiState.update { it ->
                        it.copy(
                            totalIncome = income.sumOf { it.amount },
                            totalExpense = expense.sumOf { it.amount },
                            todayDate = event.date,
                            transactions = filtered
                        )
                    }
                }
            }
            HomeScreenEvent.OpenDatePicker -> {
                _uiState.update { it ->
                    it.copy(openDatePicker = !it.openDatePicker)
                }
            }
        }
    }
}
