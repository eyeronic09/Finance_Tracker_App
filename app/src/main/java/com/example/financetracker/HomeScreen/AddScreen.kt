package com.example.financetracker.HomeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.financetracker.HomeScreen.component.addContent



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModel: TransactionViewModel,

    ) {
    val amount by viewModel.amount.collectAsState(initial = "")
    val selectedOption by viewModel.selectedOption.collectAsState()
    val categorySelected by viewModel.selectedCategory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Screen") },
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
        addContent(
            amount = amount,
            transactionType = selectedOption,
            selectedCategory = categorySelected,
            onAmountChange = { viewModel.numField(it) },
            onTransactionTypeSelected = {viewModel.onOptionSelected(it)},
            onCategorySelected = { viewModel.onSelectedCategory(it) },
            onAddClick = { viewModel.addTransaction() },
            modifier = Modifier.padding(innerPadding)
        )
    }

}