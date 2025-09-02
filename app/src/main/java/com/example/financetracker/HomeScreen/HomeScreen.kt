package com.example.financetracker.HomeScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.financetracker.HomeScreen.component.BalanceCard
import com.example.financetracker.HomeScreen.component.TransactionDetail
import com.example.financetracker.navigation.SealedScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(viewModel: TranscationViewModel , navController: NavController) {
    val transaction by viewModel.filteredTransactions.collectAsStateWithLifecycle()
    val currentBalance by viewModel.balance.collectAsStateWithLifecycle(initialValue = 0.0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finance Tracker") },
                actions = {
                    TextButton(
                        onClick = { navController.navigate(SealedScreen.summaryScreen.route) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Summary")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(SealedScreen.AddScreen.route)
                }

            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        val categories = listOf("Food", "travel", "bill", "salary", "paycheck", "other")
        val currentCategory by viewModel.selectedFilterCategory.collectAsStateWithLifecycle()
        val totalIncome by viewModel.incomeSum.collectAsStateWithLifecycle()
        val totalExpense by viewModel.expenseSum.collectAsStateWithLifecycle()
        Log.d("selectedCategory", "$currentCategory")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                BalanceCard(
                    balance = currentBalance,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    totalIncome = totalIncome,
                    totalExpenses = totalExpense,
                )
            }
            item {
                // Filter chips row
                LazyRow(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories.size) { index ->
                        val chip = categories[index]
                        Log.d("isSelected", chip)
                        val isSelected = currentCategory == chip
                        Log.d("isSelected", isSelected.toString() + chip)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onFilterCategory(if (isSelected) null else chip) },
                            label = { Text(text = chip) }
                        )
                    }
                }
            }
            items(transaction) { transaction ->
                TransactionDetail(
                    transaction = transaction,
                    onClick = { viewModel.deleteTransaction(transaction) }
                )
            }
            Log.d("BalanceHome", "$transaction")
        }
    }
}



