package com.example.financetracker.HomeScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.AddTransaction.compontent.getCategoryIcon
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.ui.theme.FinanceTrackerTheme
import com.example.financetracker.ui.theme.LocalCurrency
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TransactionDetail(
    transaction: Transaction,
    onClick: () -> Unit
) {
    val currencyInfo = LocalCurrency.current
    val currencyFormat = remember(currencyInfo) {
        NumberFormat.getCurrencyInstance(currencyInfo.locale).apply {
            maximumFractionDigits = 0
        }
    }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM, hh:mm a") }

    val isIncome = transaction.type.equals("income", ignoreCase = true)
    val amountColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)
    val iconColor = if (isIncome) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                val icon = getCategoryIcon(transaction.category)
                if (icon == Icons.Default.Category && transaction.category.isNotEmpty()) {
                    Text(
                        text = transaction.category.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = iconColor
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Details (Category & Note)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = transaction.date.format(dateFormatter),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Amount
            Text(
                text = "${if (isIncome) "+" else "-"} ${currencyFormat.format(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                ),
                color = amountColor
            )
        }
    }
}

@Preview(showBackground = true, name = "Expense")
@Composable
fun TransactionDetailExpensePreview() {
    FinanceTrackerTheme {
        TransactionDetail(
            transaction = Transaction(
                id = 1,
                amount = 1500.0,
                type = "expense",
                category = "Food",
                date = LocalDateTime.now(),
                note = "Dinner with friends"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Income")
@Composable
fun TransactionDetailIncomePreview() {
    FinanceTrackerTheme {
        TransactionDetail(
            transaction = Transaction(
                id = 2,
                amount = 50000.0,
                type = "income",
                category = "Salary",
                date = LocalDateTime.now(),
                note = "Monthly Salary"
            ),
            onClick = {}
        )
    }
}
