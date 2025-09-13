package com.example.financetracker

import android.util.Log
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financetracker.HomeScreen.TransactionViewModel
import com.example.financetracker.navigation.SealedScreen

@Composable
fun EditTransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Log.d("navBackStackEntry" , "$navBackStackEntry")

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
                navController.previousBackStackEntry
                Log.d("nav" , "$navController.previousBackStackEntry")
            }
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }
}