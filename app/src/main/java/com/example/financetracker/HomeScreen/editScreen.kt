package com.example.financetracker.HomeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.time.LocalDateTime
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel
) {
    val transaction by viewModel.transactionForEdit.collectAsStateWithLifecycle()
    val currentType by viewModel.selectedOption.collectAsStateWithLifecycle()
    val currentCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val currentAmount by viewModel.amount.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val categories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")
    val types = listOf("Expense", "Income")

    val categoryIcons = mapOf(
        "Food" to Icons.Default.Fastfood,
        "Travel" to Icons.Default.Flight,
        "Bill" to Icons.Default.Receipt,
        "Salary" to Icons.Default.Work,
        "Paycheck" to Icons.Default.AttachMoney,
        "Other" to Icons.Default.MoreHoriz
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        transaction?.let {
                            val success = viewModel.updateTransaction(it)
                            if (success) {
                                snackbarHostState.showSnackbar("Transaction updated!")
                                navController.popBackStack()
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Transaction")
            }
        }
    ) { innerPadding ->

        Card(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Amount Field
                OutlinedTextField(
                    value = currentAmount,
                    onValueChange = { viewModel.numField(it) },
                    label = { Text("Amount") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Transaction Type
                Text("Transaction Type", style = MaterialTheme.typography.titleMedium)
                SingleChoiceSegmentedButtonRow {
                    types.forEachIndexed { index, text ->
                        SegmentedButton(
                            selected = currentType == text,
                            onClick = { viewModel.onOptionSelected(text) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = types.size
                            )
                        ) {
                            Text(text)
                        }
                    }
                }

                // Category
                Text("Category", style = MaterialTheme.typography.titleMedium)
                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxWidth()) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        FilterChip(
                            selected = currentCategory == category,
                            onClick = { viewModel.onSelectedCategory(category) },
                            label = { Text(category) },
                            leadingIcon = {
                                categoryIcons[category]?.let { Icon(it, contentDescription = null) }
                            },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
