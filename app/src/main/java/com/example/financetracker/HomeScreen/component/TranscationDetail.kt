package com.example.financetracker.HomeScreen.component
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.HomeScreen.TransactionRoom.Transaction
import java.time.format.DateTimeFormatter
import kotlin.text.format
import java.time.LocalDateTime



@Composable
fun TransactionDetail(
    transaction: Transaction,
    onClickUpdate: () -> Unit ,
    onClickDelete: () -> Unit
) {

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
    val formattedDate = transaction.date.format(formatter)
    val amountColor = if (transaction.type.equals("income", ignoreCase = true))
        Color(0xFF2E7D32)
    else
        Color(0xFFC62828)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.outlinedCardElevation(4.dp),
        border = BorderStroke(1.dp, color = amountColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side
            Column {
                Text(text = transaction.id.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // right

            Text(
                text = if (transaction.type.equals("income", ignoreCase = true))
                    "+ ₹${transaction.amount}"
                else
                    "- ₹${transaction.amount}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )

        }
        Column {
            Row {
                IconButton(onClickUpdate ) {
                    Icon(Icons.Default.Edit, contentDescription = "edit")

                }
                IconButton(onClickDelete) {
                    Icon(Icons.Default.Delete , contentDescription = "delete")
                }
            }


        }
    }
}
@Preview(showBackground = true)
@Composable
private fun TransactionDetailPreview() {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/M/yyyy hh:mm:ss")
    val formattedDate = currentDateTime.format(formatter)

    TransactionDetail(
        transaction = Transaction(
            id = 1,
            amount = 100.0,
            category = "Food",
            date = currentDateTime,
            type = "expense"
        ),
        onClickUpdate = {},
        onClickDelete = {},
    )
}