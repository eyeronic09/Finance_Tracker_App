package com.example.financetracker.BudgetScreen.UI_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.BudgetScreen.Domain.model.Budget
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


data class BudgetUiState(
    val budgetId: Int = 0,
    val amount: Double = 0.0,
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val isAddBudgetDialogVisible: Boolean = false,
    val newBudgetAmount: Double = 0.0
)

sealed class BudgetEvent{
    data class OnAmountChange(val amount: Double) : BudgetEvent()
    data class OnUpdateBudget(val amount: Double) : BudgetEvent()
    data class OnDeleteBudget(val amount: Double) : BudgetEvent()
    object OnClearBudget : BudgetEvent()
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
            is BudgetEvent.OnClearBudget -> TODO()
            is BudgetEvent.OnDeleteBudget -> TODO()
            is BudgetEvent.OnAmountChange -> {
                _UiState.update { it.copy(newBudgetAmount = event.amount) }
            }
            is BudgetEvent.OnUpdateBudget -> TODO()
            is BudgetEvent.AddBudget -> AddBudget()
            is BudgetEvent.ShowAddBudgetDialog -> {
                _UiState.update { 
                    it.copy(
                        isAddBudgetDialogVisible = event.isVisible,
                        newBudgetAmount = if (event.isVisible) it.amount else 0.0
                    ) 
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _UiState.value = _UiState.value.copy(
                amount = repository.getBudget()
            )
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
            repository.setBudget(budget)
            _UiState.update { 
                it.copy(
                    amount = it.newBudgetAmount,
                    isAddBudgetDialogVisible = false 
                ) 
            }
        }
    }
}