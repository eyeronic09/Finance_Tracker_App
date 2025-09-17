package com.example.financetracker.HomeScreen.component


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addContent(
    amount: String,
    selectedCategory: String?,
    onAmountChange: (String) -> Unit,
    onTransactionTypeSelected: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val transactionType = listOf("Income", "Expense")
    var expandedForType by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(transactionType[0]) }

    val transactionCategories = listOf("Food", "Travel", "Bill", "Salary", "Paycheck", "Other")


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        TextField(
            value = amount,
            onValueChange = { newNum ->
                if (newNum.all { it.isDigit() }) {
                    onAmountChange(newNum)
                }
            },
            label = { Text("Enter Amount") },
            leadingIcon = {
                Text(
                    "$",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedForType,
                onExpandedChange = { expandedForType = !expandedForType }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true),
                    readOnly = true,
                    value = selectedOption,
                    onValueChange = {},
                    label = { Text("Select Transaction Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedForType
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                )

                ExposedDropdownMenu(
                    expanded = expandedForType,
                    onDismissRequest = { expandedForType = false }
                ) {
                    transactionType.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedOption = option
                                onTransactionTypeSelected(selectedOption)
                                expandedForType = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Select Category:", style = MaterialTheme.typography.titleMedium)

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier,
        ) {
            items(transactionCategories) { categories ->
                val isSelected = selectedCategory == categories
                FilterChip(
                    modifier = Modifier.padding(10.dp),
                    selected = selectedCategory == categories,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.inverseOnSurface
                        }
                    ),
                    onClick = {
                        onCategorySelected(categories)
                        Log.d("onCategorySelected", categories)
                    },
                    label = { Text(categories)
                            },
                )
            }
        }
        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("add")
        }
    }

}

