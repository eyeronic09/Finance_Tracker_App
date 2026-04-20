package com.example.financetracker.HomeScreen.viewmodel

import android.util.Log
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
import java.time.LocalDateTime

data class HomeScreenUiState(
    val transactions: List<Transaction> = emptyList(),
    val openDatePicker : Boolean = false,
    val selectedTransaction: Transaction? = null,
    val todayDate: LocalDate,
    val isLoading: Boolean = false,
    val TotalBalances : Double = 0.0,
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
                getOnDateSelectedTransaction(date = event.date)
            }
            HomeScreenEvent.OpenDatePicker -> {
                _uiState.update { it ->
                    it.copy(openDatePicker = !it.openDatePicker)
                }
            }
        }
    }
    init {
        getOnDateSelectedTransaction(date = LocalDate.now())
        updateTotalBalance()

    }

    fun updateTotalBalance(){
        viewModelScope.launch {
            _uiState.update { it.copy(TotalBalances = repository.getBudget(LocalDateTime.now())?: 0.0) }

        }
    }


    fun getOnDateSelectedTransaction(date: LocalDate){
        viewModelScope.launch {
            val all = repository.getAllTransactions()

            val filtered = all.filter { it.date.toLocalDate() == date }
            val income =  filtered.filter { it.type.equals("income", ignoreCase = true) }
            val expense = filtered.filter { it.type.equals("expense"  , true) }

            val totalIncome = income.sumOf { it.amount }
            val totalExpense = expense.sumOf { it.amount }

            _uiState.update { it ->
                it.copy(
                    balance = totalIncome - totalExpense,
                    totalIncome = totalIncome,
                    totalExpense = totalExpense,
                    todayDate = date,
                    transactions = filtered
                )
            }
            Log.d("BalanceCard", _uiState.value.toString())
        }
    }

}
