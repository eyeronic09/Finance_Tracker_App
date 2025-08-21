package com.example.financetracker.HomeScreen.component
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction  // Make sure this path is correct


@Composable
fun TranscationsDetail(transaction: Transaction , onClick: () -> Unit) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Amount: ${transaction.amount}")
            Text("Type: ${transaction.type}")
            Text("Date: ${transaction.date}")
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = Color.Gray
        )
        IconButton(
            onClick =  onClick
        ) {
            Icon(
                Icons.Default.Delete ,
                null)
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun prevs() {

    TranscationsDetail(
        transaction = Transaction(
            1,
            amount = 121.9,
            type = "Income",
            date = 123,

            ),
        onClick = {}
    )
}