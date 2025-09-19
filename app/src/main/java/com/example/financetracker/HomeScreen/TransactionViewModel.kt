package com.example.financetracker.HomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * The TransactionViewModel is responsible for managing the state of the amount for transactions.
 * It uses the MutableStateFlow class to create a state flow which allows us to observe changes
 * in the amount. The getter method for the amount property returns the state flow as a StateFlow.
 * This allows us to easily observe changes in the amount using the `collect` function.
 */

class TransactionViewModel(
    private val transactionDao: TranscationDao
) : ViewModel() {
    private val allTransactions: StateFlow<List<Transaction>> = transactionDao.getAll()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val _transactionForEdit = MutableStateFlow<Transaction?>(null)
    val transactionForEdit: StateFlow<Transaction?> = _transactionForEdit.asStateFlow()
    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption

    private val _category = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _category
    fun onSelectedCategory(category: String) {
        _category.value = category
    }

    private val _selectedFilterCategory = MutableStateFlow<String?>(null)
    val selectedFilterCategory: StateFlow<String?> = _selectedFilterCategory.asStateFlow()


    val filteredTransactions =
        combine(allTransactions, selectedFilterCategory) { transactions, category ->
            if (category == null) {
                transactions
            } else {
                transactions.filter { it.category.equals(category, ignoreCase = true) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onFilterCategory(category: String?) {
        _selectedFilterCategory.value = category
    }


    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage: SharedFlow<String?> = _errorMessage.asSharedFlow()


    fun onOptionSelected(option: String) {
        _selectedOption.value = option
    }

    private val _amount = MutableStateFlow("")
    var amount: StateFlow<String> = _amount.asStateFlow()
    fun numField(newAmount: String) {
        _amount.value = newAmount
        Log.d("Amount", newAmount)
    }

    // Clear form fields
    private fun resetForm() {
        _amount.value = ""
        _selectedOption.value = null
        _category.value = null
    }


    suspend fun clearErrorMessage() {
        _errorMessage.emit(null)
    }

    val balance: StateFlow<Double> = filteredTransactions.map { transactions ->
        transactions.fold(0.0) { acc, transaction ->
            val amount = transaction.amount
            if (transaction.type.equals("income", ignoreCase = true)) {
                acc + amount
            } else {
                acc - amount
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = 0.0
    )

    // Balance, income, and expense totals for filtered transactions
    val incomeSum: StateFlow<Double> = filteredTransactions.map { transactions ->
        transactions.filter { it.type.equals("Income", true) }.sumOf { it.amount }
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(200), 0.0)

    val expenseSum: StateFlow<Double> = filteredTransactions.map { transactions ->
        transactions.filter { it.type.equals("expense", true) }.sumOf {
            it.amount
        }
    }.stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(200), 0.0)

    /**
     * Adds a transaction to the database.
     */

    // --- Actions ---
    fun addTransaction() {
        viewModelScope.launch {
            try {
                val amountValue = _amount.value.toDoubleOrNull()
                val type = _selectedOption.value
                val category = _category.value ?: "other"
                val dateTime: LocalDateTime = LocalDateTime.now()

                if (amountValue == null || amountValue <= 0.0) {
                    _errorMessage.emit("Please enter a positive amount")
                    return@launch
                }
                if (type == null) {
                    _errorMessage.emit("Select income or expense")
                    return@launch
                }

                val newTransaction = Transaction(
                    amount = amountValue,
                    type = type,
                    date = dateTime,
                    category = category
                )

                transactionDao.insert(newTransaction)

                // reset form
                resetForm()
                _errorMessage.emit(null)

            } catch (e: Exception) {
                _errorMessage.emit("Error saving transaction")
                Log.e("TransactionViewModel", "Error adding transaction", e)
            }
        }
    }
    fun setTransactionForEditing(transaction: Transaction) {
        _transactionForEdit.value = transaction
        _amount.value = transaction.amount.toString()
        _selectedOption.value = transaction.type
        _category.value = transaction.category
    }

    /**
     * Updates an existing transaction with the current form values
     * @param transaction The original transaction to be updated
     * @return Boolean indicating if the update was successful
     */
    suspend fun updateTransaction(transaction: Transaction): Boolean {
        return try {
            val amountValue = _amount.value.toDoubleOrNull()
            val type = _selectedOption.value
            val category = _category.value ?: "other"

            // Input validation
            if (amountValue == null || amountValue <= 0) {
                _errorMessage.emit("Please enter a valid positive amount")
                return false
            }

            if (type == null) {
                _errorMessage.emit("Please select a transaction type")
                return false
            }

            val updatedTransaction = transaction.copy(
                amount = amountValue,
                type = type,
                category = category,
                date = LocalDateTime.now() // Update the timestamp
            )

            transactionDao.updatetoBalance(updatedTransaction)
            resetForm()
            true

        } catch (e: Exception) {
            Log.e("TransactionViewModel", "Error updating transaction", e)
            _errorMessage.emit("Failed to update transaction: ${e.message}")
            false
        }
    }


    /**
     * Deletes a transaction from the database.
     *
     * @param transaction The transaction to be deleted.
     */
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionDao.delete(transaction)
        }
    }

}