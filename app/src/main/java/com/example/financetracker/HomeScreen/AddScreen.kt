package com.example.financetracker.HomeScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.financetracker.HomeScreen.component.AddContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModel: TransactionViewModel,

    ) {
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val setelectedType by viewModel.selectedOption.collectAsStateWithLifecycle()
    val categorySelected by viewModel.selectedCategory.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addTransaction() }) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        AddContent(
            amount = amount,
            selectedCategory = categorySelected,
            onAmountChange = { viewModel.numField(it) },
            onTransactionTypeSelected = { viewModel.onOptionSelected(it) },
            onCategorySelected = { viewModel.onSelectedCategory(it) },
            modifier = Modifier.padding(innerPadding)
        )
    }

}

