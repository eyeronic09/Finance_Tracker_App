package com.example.financetracker.Summary.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.financetracker.Summary.SummaryModel.TransactionChartViewModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer

@Composable
fun SummaryChartScreen( viewModel: TransactionChartViewModel) {
    val categoryTotals by viewModel.categoryTotalAndLable.collectAsState()
    val modelProducer = remember { CartesianChartModelProducer() }

    Column {
        Text("this chart Screen $categoryTotals , $modelProducer")
    }
}