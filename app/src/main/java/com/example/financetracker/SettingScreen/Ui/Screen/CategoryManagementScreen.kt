package com.example.financetracker.SettingScreen.Ui.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.financetracker.AddTransaction.compontent.getCategoryIcon
import com.example.financetracker.SettingScreen.Ui.Screen.ViewModel.CategoryManagementVM
import com.example.financetracker.core.domain.model.Category
import com.example.financetracker.ui.theme.LocalCurrency
import org.koin.androidx.compose.koinViewModel

class CategoryManagementScreen : Screen {
    @Composable
    override fun Content() {
        CategoryManagementRoute()
    }
}

@Composable
fun CategoryManagementRoute(
    viewModel: CategoryManagementVM = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    CategoryManagementScreenContent(
        uiState = uiState,
        onBackClick = { navigator?.pop() },
        onUpdateLimit = viewModel::updateCategoryLimit,
        onAddCategory = viewModel::addCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreenContent(
    uiState: com.example.financetracker.SettingScreen.Ui.Screen.ViewModel.CategoryManagementUiState,
    onBackClick: () -> Unit,
    onUpdateLimit: (String, Double) -> Unit,
    onAddCategory: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.categories) { category ->
                    CategoryLimitItem(
                        category = category,
                        onUpdateLimit = onUpdateLimit
                    )
                }
            }
        }

        if (showAddDialog) {
            AddCategoryDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name ->
                    onAddCategory(name)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Category") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Category Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CategoryLimitItem(
    category: Category,
    onUpdateLimit: (String, Double) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showDialog = true }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getCategoryIcon(category.name),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Limit: ${LocalCurrency.current.symbol}${category.budgetLimit}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showDialog) {
        CategoryLimitDialog(
            category = category,
            onDismiss = { showDialog = false },
            onConfirm = { limit ->
                onUpdateLimit(category.name, limit)
                showDialog = false
            }
        )
    }
}

@Composable
fun CategoryLimitDialog(
    category: Category,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var limitText by remember { mutableStateOf(category.budgetLimit.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Limit for ${category.name}") },
        text = {
            OutlinedTextField(
                value = limitText,
                onValueChange = { limitText = it },
                label = { Text("Budget Limit") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text(LocalCurrency.current.symbol) }
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val limit = limitText.toDoubleOrNull() ?: 0.0
                onConfirm(limit)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
