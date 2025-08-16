package com.example.financetracker.HomeScreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun HomeScreen(viewModel: TranscationViewModel) {
    val Trancation by viewModel.allTransactions.collectAsStateWithLifecycle()
    Text("$Trancation")

}