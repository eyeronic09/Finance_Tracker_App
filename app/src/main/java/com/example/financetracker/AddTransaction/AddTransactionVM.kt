package com.example.financetracker.AddTransaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
enum class TransactionType {
    income, expense;

    companion object {
        fun fromString(value: String): TransactionType {
            return when (value.lowercase()) {
                "income" -> income
                "expense", "expenses" -> expense
                else -> throw IllegalArgumentException("No enum constant for $value")
            }
        }
    }
}
data class AddTransactionUiState(
    val toastMessage: String? = null,
    val transactionType: TransactionType = TransactionType.income,
    val category: String = "",
    val availableCategories : List<String> = emptyList(),
    val selectedDate : LocalDateTime ,
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
class AddTransactionVM (val categoryRepository: TransactionRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddTransactionUiState(selectedDate = LocalDateTime.now()))
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event: SharedFlow<String> = _event.asSharedFlow()

    fun onEvent(onEvent: AddTransactionEvent) {
        when (onEvent) {
            is AddTransactionEvent.OnCategoryChange -> {
                _uiState.update { it ->
                    it.copy(category = onEvent.category)
                }
            }
            is AddTransactionEvent.OnTransactionNoteChange -> {
                _uiState.update { it ->
                    it.copy(transactionNote = onEvent.transactionNote)
                }

            }
            is AddTransactionEvent.OnTransactionTypeChange -> {
                _uiState.update { it ->
                    it.copy(transactionType = onEvent.transactionType)
                }
                getCategories()
            }
            is AddTransactionEvent.saveTransaction -> {
                saveTransaction()
            }
            is AddTransactionEvent.amountChange -> {
                _uiState.update { it ->
                    it.copy(amount = onEvent.amount)
                }
            }
        }
    }
    init {
        viewModelScope.launch {
            getCategories()
        }
    }
    private fun getCategories() {
        viewModelScope.launch {
            val allCategories = categoryRepository.getAlltheCategory()
            val filtered = if (_uiState.value.transactionType == TransactionType.income) {
                allCategories.filter {it ->  it.type == TransactionType.income }
            } else {
                allCategories.filter { it ->  it.type == TransactionType.expense }
            }
            _uiState.update { it ->
                it.copy(availableCategories = filtered.map { it -> it.name })
            }
        }
    }
    private fun saveTransaction() {
        val state = _uiState.value

        viewModelScope.launch {
            when {
                state.amount == 0.0 || state.amount <= 0.0 -> {
                    _uiState.update { it ->
                        it.copy(toastMessage = "Please enter a valid amount")
                    }
                    _event.emit(state.toastMessage.toString())
                }

                state.transactionType.name.isEmpty() -> {
                    _uiState.update { it ->
                        it.copy(toastMessage = "Please select a transaction type or category")
                    }
                    _event.emit(state.toastMessage.toString())
                }

                else -> {
                    val newTransaction = Transaction(
                        id = 0,
                        amount = state.amount,
                        type = state.transactionType.name,
                        category = state.category,
                        date = state.selectedDate,
                        note = state.transactionNote.toString()
                    )
                    categoryRepository.insertTransaction(newTransaction)
                    _event.emit("Saved successfully")
                    _uiState.update { it ->
                        it.copy(
                            amount = 0.0,
                            transactionType = TransactionType.income,
                            category = "",
                            availableCategories = emptyList(),
                            selectedDate = LocalDateTime.now(),
                            transactionNote = null
                        )
                    }
                }
            }
        }
        Log.d("AddTransactionVM", "Saving transaction: $state")
    }
}
