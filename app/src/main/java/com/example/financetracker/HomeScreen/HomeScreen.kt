package com.example.financetracker.HomeScreen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.financetracker.HomeScreen.component.TranscationsDetail
import com.example.financetracker.navigation.SealedScreen


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(viewModel: TranscationViewModel , navController: NavController) {
    val transaction by viewModel.allTransactions.collectAsStateWithLifecycle()
    val currentBalance by viewModel.balance.collectAsStateWithLifecycle(initialValue = 0.0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Finance Tracker")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(SealedScreen.AddScreen.route)
                }

            ) {
                Icon(Icons.Default.Add , contentDescription = null)
            }
        }
    ) { innerPadding ->

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)) {
            item {
                BalanceCard(
                    balance = currentBalance,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            items(transaction){ transaction ->
                TranscationsDetail(
                    transaction = transaction,
                    onClick = {viewModel.deleteTransaction(transaction)}
                )
            }
            Log.d("BalanceHome" , "$currentBalance")
        }
    }

}