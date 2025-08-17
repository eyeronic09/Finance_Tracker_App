package com.example.financetracker.HomeScreen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import com.example.financetracker.HomeScreen.component.BalanceCard



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(viewModel: TranscationViewModel) {
    val transaction by viewModel.allTransactions.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Finance Tracker")}
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(Icons.Default.Add , contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)) {
            items(transaction){ balanceCard ->
                BalanceCard(transaction = balanceCard)
            }
        }

    }

}