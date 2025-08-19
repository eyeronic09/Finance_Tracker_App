package com.example.financetracker.HomeScreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.temporal.TemporalAmount

@Composable
fun addContent(
    amount: String,
    selectedOption: String?,
    onAmountChange: (String) -> Unit,
    onOptionSelected: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val radioOptions = listOf("income", "expense")

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
            radioOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(option) }
                        .padding(8.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == option),
                        onClick = { onOptionSelected(option) }
                    )
                    Text(text = option)
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddClick,
            enabled = amount.isNotBlank() && selectedOption != null
        ) {
            Text("Add")
        }
    }
}
