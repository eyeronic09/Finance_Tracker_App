package com.example.financetracker.BudgetScreen.UI_Screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.BudgetScreen.Domain.model.CategoryBudget
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryBudgetUiState(
    val categoryBudget: List<CategoryBudget> = emptyList()
)

class BudgetChartVM(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _UiState = MutableStateFlow(CategoryBudgetUiState())
    val CategoryBudgetUiState: StateFlow<CategoryBudgetUiState> = _UiState.asStateFlow()

    init {
        fetchCategoryBudgets()
    }

    fun fetchCategoryBudgets() {
        viewModelScope.launch {
            val budgets = repository.getAllTheTransitionOfCurrentMonths()
            
            // Group by category name and map to a List of CategoryBudget objects
            val categoryList = budgets.groupBy { it.categoryName }.map { (name, items) ->
                CategoryBudget(
                    categoryName = name,
                    sum = items.sumOf { it.sum },
                    icon = items.first().icon
                )
            }
            
            _UiState.update { it.copy(categoryBudget = categoryList) }
        }
    }
}
