package com.example.financetracker.AddTransaction

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class TransactionType {
    INCOME , EXPENSES
}
data class AddTransactionUiState(
    val transactionType: TransactionType = TransactionType.INCOME,
    val category: String = "",
    val transactionNote : String? = null,
    val amount: Double = 0.0
)

sealed interface AddTransactionEvent {
data class amountChange(val amount: Double) : AddTransactionEvent
    data class OnTransactionTypeChange(val transactionType: TransactionType) : AddTransactionEvent
    data class OnCategoryChange(val category: String) : AddTransactionEvent
    data class OnTransactionNoteChange(val transactionNote: String) : AddTransactionEvent
    object saveTransaction : AddTransactionEvent
}
class AddTransactionVM () : ViewModel() {
    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    fun onEvent(onEvent: AddTransactionEvent) {
        when (onEvent) {
            is AddTransactionEvent.OnCategoryChange -> TODO()
            is AddTransactionEvent.OnTransactionNoteChange -> TODO()
            is AddTransactionEvent.OnTransactionTypeChange -> TODO()
            is AddTransactionEvent.saveTransaction -> TODO()
            is AddTransactionEvent.amountChange -> {
                _uiState.update { it ->
                    it.copy(amount = onEvent.amount)
                }
            }
        }
    }
}