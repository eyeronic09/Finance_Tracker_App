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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financetracker.HomeScreen.TransactionViewModel
import com.example.financetracker.navigation.SealedScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun EditTransactionScreen(
    viewModel: TransactionViewModel,
    navController: NavController
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // Collect the transaction being edited
    val transaction by viewModel.transactionForEditing.collectAsStateWithLifecycle()
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    // for snack bar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var errorMessage = viewModel.errorMessage.collectAsStateWithLifecycle(null)
    LaunchedEffect(errorMessage) {
        val result = errorMessage.value?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearErrorMessage()
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

            Button(
                onClick = {
                    scope.launch {
                        val amountValue = amount.toDoubleOrNull()
                        if (amountValue == null || amountValue <= 0) {
                            snackbarHostState.showSnackbar("Please enter a valid positive number")
                            return@launch
                        }
                        viewModel.updateTransaction(
                            updatedAmount = amountValue,
                            updatedType = selectedOption ?: "expense",
                            updatedCategory = selectedCategory ?: "Other"
                        )
                        snackbarHostState.showSnackbar("updated")
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