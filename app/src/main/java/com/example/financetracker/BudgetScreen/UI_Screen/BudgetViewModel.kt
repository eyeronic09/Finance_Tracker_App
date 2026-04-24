package com.example.financetracker.BudgetScreen.UI_Screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.BudgetScreen.Domain.model.Budget
import com.example.financetracker.BudgetScreen.Domain.model.CategoryBudget
import com.example.financetracker.core.domain.model.Category
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


data class BudgetUiState(
    val budgetId: Int = 0,
    val amount: Double? = 0.0,
    val spend : Double? = 0.0,
    val remaining: Double? =0.0,
    val listOfCategory : List<CategoryBudget> = emptyList(),
    val startDate: LocalDateTime = LocalDateTime.now().withDayOfMonth(1),
    val endDate: LocalDateTime = LocalDateTime.now().plusMonths(1),
    val isAddBudgetDialogVisible: Boolean = false,
    val newBudgetAmount: Double = 0.0
)

sealed class BudgetEvent{
    data class OnAmountChange(val amount: Double) : BudgetEvent()
    data class OnUpdateBudget(val amount: Double) : BudgetEvent()
    object AddBudget : BudgetEvent()
    data class ShowAddBudgetDialog(val isVisible: Boolean) : BudgetEvent()
}
class BudgetViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _UiState = MutableStateFlow(BudgetUiState())
    val uiState : StateFlow<BudgetUiState> = _UiState.asStateFlow()

    fun onEvent( event: BudgetEvent) {
        when(event) {

            is BudgetEvent.OnAmountChange -> {
                _UiState.update { it.copy(newBudgetAmount = event.amount) }
            }
            is BudgetEvent.OnUpdateBudget -> getAllTheBudget()
            is BudgetEvent.AddBudget -> AddBudget()
            is BudgetEvent.ShowAddBudgetDialog -> {
                _UiState.update { 
                    it.copy(
                        isAddBudgetDialogVisible = event.isVisible,
                    ) 
                }
            }
        }
    }

    fun getAllTheBudget() {
        viewModelScope.launch {
            val now = LocalDateTime.now()
            val budget = repository.getBudget(now) ?: 0.0
            val spend = repository.getAllTransactionsExpensesOfThisMonth() ?: 0.0

            _UiState.update { it.copy(
                amount = budget,
                spend = spend,
                remaining = budget - spend
            )}
        }
    }

    init {
        viewModelScope.launch {
            getAllTheBudget()
            Log.d("category" , repository.getAllTheTransitionOfCurrentMonths().toString())
            calculateCategoryTotals()
        }
    }
    fun calculateCategoryTotals(){
        viewModelScope.launch {
            val data = repository.getAllTheTransitionOfCurrentMonths()
            _UiState.update { it -> it.copy(listOfCategory = data) }

        }

    }
    fun AddBudget(){
        viewModelScope.launch {
            val budget = Budget(
                budgetId = _UiState.value.budgetId,
                amount = _UiState.value.newBudgetAmount,
                startDate = _UiState.value.startDate,
                endDate = _UiState.value.endDate
            )
            repository.setBudget(budget , local = LocalDateTime.now())
            _UiState.update { 
                it.copy(
                    amount = it.newBudgetAmount,
                    isAddBudgetDialogVisible = false
                ) 
            }
        }
    }
}