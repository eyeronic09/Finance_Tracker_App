package com.example.financetracker.HomeScreen.TransactionRoom

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


/**
 * The TranscationViewModel is responsible for managing the state of the amount for transactions.
 * It uses the MutableStateFlow class to create a state flow which allows us to observe changes
 * in the amount. The getter method for the amount property returns the state flow as a StateFlow.
 * This allows us to easily observe changes in the amount using the `collect` function.
 */
class TranscationViewModel : ViewModel() {
    private val _amount = MutableStateFlow(0) // Initializes the amount to 0
    val amount: StateFlow<Int>  get() = _amount.asStateFlow() // Returns the state flow as a StateFlow


}