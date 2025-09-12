package com.example.financetracker

import androidx.compose.runtime.collectAsState

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.financetracker.HomeScreen.TransactionViewModel

@Composable
fun EditTransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {
    // Collect the transaction being edited
    val transaction by viewModel.transactionForEditing.collectAsState()

    // If no transaction is set for editing, show error and navigate back
    LaunchedEffect(transaction) {
        if (transaction == null) {
            navController.popBackStack()
        }
    }

    if (transaction == null) return


    val amount by viewModel.amount.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Form fields
        OutlinedTextField(
            value = amount,
            onValueChange = { viewModel.numField(it) },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.updateTransaction(
                    updatedAmount = amount.toDoubleOrNull() ?: 0.0,
                    updatedType = selectedOption ?: "expense",
                    updatedCategory = selectedCategory ?: "Other"
                )
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Update Transaction")
        }
        
        IconButton(
            onClick = {
                viewModel.clearEditingState()
                navController.navigateUp() 
            }
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }
}