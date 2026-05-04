package com.example.financetracker.SettingScreen.Ui.Screen.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.AddTransaction.TransactionType
import com.example.financetracker.core.domain.model.Category
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryManagementUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false
)

class CategoryManagementVM(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryManagementUiState())
    val uiState: StateFlow<CategoryManagementUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val categories = repository.getAlltheCategory()
            Log.d("CategoryManagementVM", "Loaded ${categories.size} total categories")
            
            val expenseCategories = categories.filter { category -> 
                category.type == TransactionType.expense
            }
            Log.d("CategoryManagementVM", "Filtered to ${expenseCategories.size} expense categories")

            _uiState.update { 
                it.copy(
                    categories = expenseCategories,
                    isLoading = false 
                ) 
            }
        }
    }

    fun updateCategoryLimit(categoryName: String, limit: Double) {
        viewModelScope.launch {
            repository.updateCategoryBudgetLimit(categoryName, limit)
            loadCategories()
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            val newCategory = Category(
                name = name,
                type = TransactionType.expense,
                budgetLimit = 0.0
            )
            repository.insertCategory(newCategory)
            loadCategories()
        }
    }
}
