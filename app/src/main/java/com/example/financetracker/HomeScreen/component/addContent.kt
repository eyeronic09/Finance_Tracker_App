package com.example.financetracker.HomeScreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.temporal.TemporalAmount

@Composable
fun addContent(
    amount: String,
    transactionType: String?,
    selectedCategory: String?,
    onAmountChange: (String) -> Unit,
    onTransactionTypeSelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val transactionTypes = listOf("Income", "Expense")
    val categories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = amount,
            onValueChange = { newNum ->
                if (newNum.all { it.isDigit() }) {
                    onAmountChange(newNum)
                }
            },
            label = { Text("Amount") },
            leadingIcon = { Text("$", style = MaterialTheme.typography.bodyLarge) },
            modifier = Modifier.fillMaxWidth()
        )


        Column {
            // Transaction Type (Income/Expense) radio buttons
            transactionTypes.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTransactionTypeSelected(option) }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = (transactionType == option),
                        onClick = { onTransactionTypeSelected(option) }
                    )
                    Text(text = option)
                }
            }
            
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = Color.Gray
            )
            
            // Category radio buttons
            categories.forEach { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category) }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = (selectedCategory == category),
                        onClick = { onCategorySelected(category) }
                    )
                    Text(text = category)
                }
            }
        }
        
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddClick,
            enabled = amount.isNotBlank() && transactionType != null
        ) {
            Text("Add")
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewAddContent() {
    addContent(
        amount = "12",
        transactionType = "expense",
        selectedCategory = "travel",
        onAmountChange = {},
        onTransactionTypeSelected = {},
        onCategorySelected = {},
        onAddClick = {}
    )
}
