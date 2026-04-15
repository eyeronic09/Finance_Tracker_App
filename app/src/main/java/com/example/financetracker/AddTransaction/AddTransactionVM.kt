package com.example.financetracker.AddTransaction

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
           if (state.amount >= 0.0 && state.category.isNotEmpty() ) {
               val newTransaction = Transaction(
                   id = 0,
                   amount = state.amount,
                   type = state.transactionType.name,
                   category = state.category,
                   date = state.selectedDate ,
                   note = state.transactionNote.toString()
               )
               categoryRepository.insertTransaction(newTransaction)

           }else{
               Log.d("tag" , "sorry")
           }
        }
    }
}
