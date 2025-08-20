package com.example.financetracker.HomeScreen.component
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction  // Make sure this path is correct


@Composable
fun BalanceCard(transaction: Transaction) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Amount: ${transaction.amount}")
            Text("Type: ${transaction.type}")
            Text("Date: ${transaction.date}")
        }
    }
}

@Preview
@Composable
private fun prevs() {
    BalanceCard(
        transaction = Transaction(
            1,
            amount = 121.9,
            type = "Income",
            date = 123
        )
    )
}