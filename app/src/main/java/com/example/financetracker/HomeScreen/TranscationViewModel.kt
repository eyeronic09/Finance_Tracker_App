package com.example.financetracker.HomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.TransactionRoom.TranscationDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    /**
     * `allTransactions` is a state flow that represents the list of all transactions in the database.
     * It is initialized with an empty list as the initial value, and it is updated whenever the database
     * changes. The flow is shared while there are active subscribers, and it is refreshed every 5 seconds.
     *
     * The `map` function is used to transform the flow of transactions into a flow of lists of transactions.
     * This transformation is necessary because the `getAll` function returns a flow of transactions,
     * and we want to work with a flow of lists of transactions.
     *
     * The `stateIn` function is used to collect the flow of lists of transactions and store the latest
     * value in memory.
     */
    val allTransactions: StateFlow<List<Transaction>> = transcationDao.getAll()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Adds a transaction to the database.
     *
     * @param transaction The transaction to be added.
     */
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transcationDao.insert(transaction)
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
            transcationDao.update(transaction)
        }
    }
}