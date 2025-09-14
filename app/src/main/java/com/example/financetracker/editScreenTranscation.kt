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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financetracker.HomeScreen.TransactionViewModel
import com.example.financetracker.navigation.SealedScreen

@Composable
fun EditTransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {
    // Log the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.let {
        Log.d("navBackStackEntry", "$it")
    }
    // Collect the transaction being edited
    val transaction by viewModel.transactionForEditing.collectAsStateWithLifecycle()




    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

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
                navController.popBackStack(
                    route = SealedScreen.HomeScreen.route,
                    inclusive = false
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
                // Clear the editing state
                viewModel.clearEditingState()
                navController.popBackStack(route = SealedScreen.HomeScreen.route , inclusive = false)

            }
        ) {
            // Display the arrow back icon
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }
}