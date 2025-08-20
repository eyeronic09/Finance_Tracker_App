package com.example.financetracker.HomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * The TranscationViewModel is responsible for managing the state of the amount for transactions.
 * It uses the MutableStateFlow class to create a state flow which allows us to observe changes
 * in the amount. The getter method for the amount property returns the state flow as a StateFlow.
 * This allows us to easily observe changes in the amount using the `collect` function.
 */
class TranscationViewModel(
    private val transcationDao: TranscationDao
) : ViewModel() {
    private val _balance = MutableStateFlow(0.0)
    val balance: Flow<Double?> = transcationDao.getLatestBalance()
    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption

    fun onOptionSelected(option: String){
        _selectedOption.value = option
    }

    private val _amount  = MutableStateFlow("")
    var amount : StateFlow<String> = _amount.asStateFlow()
    fun numField(newAmount: String) {
        _amount.value = newAmount
        Log.d("Amount", newAmount)
    }


    val allTransactions: StateFlow<List<Transaction>> = transcationDao.getAll()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    /**
     * Adds a transaction to the database.
     */

    fun addTransaction() {
        viewModelScope.launch {
            try {
                val amountValue = _amount.value.toDoubleOrNull()
                val type = _selectedOption.value


                if (amountValue != null && amountValue > 0 && type != null) {
                    val balance = _balance.value
                    val newBalance = if(type == "income"){
                        balance + amountValue
                    }else{
                        balance - amountValue
                    }
                    val newTransaction = Transaction(
                        amount = amountValue,
                        type = type,
                        date = System.currentTimeMillis(),
                        balance = newBalance
                    )
                    Log.d("Balance" , "$newTransaction")
                    transcationDao.insert(newTransaction)
                    // reset
                    _amount.value = ""
                    _selectedOption.value = null
                }
            } catch (e: Exception) {
                Log.d("TranscationViewModel", "Error adding transaction", e)
            }
        }
    }


    /**
     * Deletes a transaction from the database.
     *
     * @param transaction The transaction to be deleted.
     */
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transcationDao.delete(transaction)
        }
    }

    /**
     * Updates a transaction in the database.
     *
     * @param transaction The transaction to be updated.
     */
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transcationDao.updatetoBalance(transaction)
        }
    }
}