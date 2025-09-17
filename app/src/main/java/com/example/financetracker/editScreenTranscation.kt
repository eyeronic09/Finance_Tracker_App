package com.example.financetracker

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financetracker.HomeScreen.TransactionViewModel
import com.example.financetracker.navigation.SealedScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EditTransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {
    // UI State
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    // Collect state from ViewModel
    val transaction by viewModel.transactionForEditing.collectAsStateWithLifecycle()
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val isUpdating by viewModel.isUpdating.collectAsStateWithLifecycle()
    
    // Local state
    var selectedType by remember { mutableStateOf("Expense") }
    var selectedCategory by remember { mutableStateOf("Other") }

    val categories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")
    val transactionTypes = listOf("Income", "Expense")

    // Handle error messages
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle(null)
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearErrorMessage()
        }
    }
    
    // Initialize form with transaction data
    LaunchedEffect(transaction) {
        transaction?.let { 
            selectedType = it.type
            selectedCategory = it.category
        }
    }
    
    // Handle successful update
    LaunchedEffect(isUpdating) {
        if (!isUpdating && transaction == null) {
             navController.popBackStack()
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Form fields
            OutlinedTextField(
                value = amount,
                onValueChange = { viewModel.numField(it) },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            Column {
                Text(
                    "Transaction Type",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                transactionTypes.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedType = option }
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedType == option),
                            onClick = { selectedType = option }
                        )
                        Text(text = option, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Category",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                categories.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = category }
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (selectedCategory == category),
                            onClick = { selectedCategory = category }
                        )
                        Text(text = category, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val amountValue = amount.toDoubleOrNull()
                            if (amountValue == null || amountValue <= 0) {
                                snackbarHostState.showSnackbar("Please enter a valid positive number")
                                return@launch
                            }

                            viewModel.updateTransaction(
                                updatedAmount = amountValue,
                                updatedType = selectedType,
                                updatedCategory = selectedCategory
                            )


                        } catch (e: Exception) {
                            Log.e("EditScreen", "Error updating transaction", e)
                            snackbarHostState.showSnackbar("Error updating transaction: ${e.message}")
                        }
                    }
                }
            ) {
                Text("Update Transaction")
            }
            IconButton(
                onClick = {
                    // Clear the editing state
                    viewModel.clearEditingState()
                    navController.popBackStack(
                        route = SealedScreen.HomeScreen.route,
                        inclusive = false
                    )

                }
            ) {
                // Display the arrow back icon
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }


    }

}