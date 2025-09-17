package com.example.financetracker

import android.util.Log
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.financetracker.HomeScreen.TransactionViewModel
import com.example.financetracker.navigation.SealedScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    var expandedForType by remember { mutableStateOf(false) }

    val categories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")
    val transactionTypes = listOf("Income", "Expense")

    val categoryIcons = mapOf(
        "Food" to Icons.Default.Fastfood,
        "Travel" to Icons.Default.DirectionsCar,
        "Bill" to Icons.Default.Receipt,
        "Salary" to Icons.Default.Work,
        "Paycheck" to Icons.Default.AttachMoney,
        "Other" to Icons.Default.MoreHoriz
    )

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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
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
                    ExposedDropdownMenuBox(
                        expanded = expandedForType,
                        onExpandedChange = { expandedForType = !expandedForType }
                    ) {
                        OutlinedTextField(
                            value = transactionTypes[1],
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Transaction Type") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedForType) },
                            modifier = Modifier
                                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                        )

                        ExposedDropdownMenu(
                            expanded = expandedForType,
                            onDismissRequest = { expandedForType = false }
                        ) {
                            transactionTypes.forEach { option ->
                                DropdownMenuItem(
                                    leadingIcon = {
                                        if (option == "Income") {
                                            Icon(Icons.Default.ArrowUpward, null, tint = androidx.compose.ui.graphics.Color.Green )
                                        } else {
                                            Icon(Icons.Default.ArrowDownward, null, tint = androidx.compose.ui.graphics.Color.Red )
                                        }
                                    },
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onOptionSelected(option)
                                        expandedForType = false
                                    }
                                )
                            }
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
                    LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 120.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.onSelectedCategory(category) },
                            label = { Text(category) },
                            leadingIcon = {
                                categoryIcons[category]?.let {
                                    Icon(imageVector = it, contentDescription = category)
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
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

}
