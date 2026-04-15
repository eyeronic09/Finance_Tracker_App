package com.example.financetracker.AddTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import com.example.financetracker.AddTransaction.compontent.RowScrollDropdown
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

class _AddTranscationScreen : Screen {
    @Composable
    override fun Content() {
        AddTransactionScreenRoute()
    }
}

@Composable
fun AddTransactionScreenRoute(viewModel: AddTransactionVM = koinViewModel()){
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent
    AddTranscationScreen(
        state = state,
        onEvent = event
    )
}
@Composable
fun AddTranscationScreen(
    state : AddTransactionUiState ,
    onEvent: (AddTransactionEvent) -> Unit ,
    modifier: Modifier = Modifier) {
    Scaffold(

    ) { it ->
        AddTransactionContent(
            state = state,
            onEvent = onEvent,
            modifier = modifier.padding(paddingValues = it)
        )
    }
}

@Composable
fun AddTransactionContent(
    state : AddTransactionUiState ,
    onEvent: (AddTransactionEvent) -> Unit ,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier
        .fillMaxSize()
        .padding()){
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = if (state.amount == 0.0) "" else state.amount.toString(),
            onValueChange = { s: String ->
                val amount = s.toDoubleOrNull() ?: 0.0
                onEvent(AddTransactionEvent.amountChange(amount))
            },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TransactionTypeRadioButtons(
            selectedRadioButton = state.transactionType,
            onClick = {
                onEvent(AddTransactionEvent.OnTransactionTypeChange(it))
            }
        )
        RowScrollDropdown(
            selectedCategory = state.category,
            categories = state.availableCategories,
            onCategorySelected = {
                onEvent(AddTransactionEvent.OnCategoryChange(it))
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            label = { Text("Note") },
            value = state.transactionNote.toString(),
            onValueChange = {
            onEvent(AddTransactionEvent.OnTransactionNoteChange(it))
        })
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(AddTransactionEvent.saveTransaction) }
        ) {
            Text(text = "Save Transaction")
        }

    }
}



@Composable
fun TransactionTypeRadioButtons(
    selectedRadioButton : TransactionType,
    onClick : (TransactionType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
        ,
        ) {
        TransactionTypeItem(
            text = "Income",
            isSelected = selectedRadioButton == TransactionType.income,
            onClick = { onClick(TransactionType.income)},
            modifier = Modifier.weight(1f)
        )
        TransactionTypeItem(
            text = "Expense",
            isSelected = selectedRadioButton == TransactionType.expense,
            onClick = { onClick(TransactionType.expense)},
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable()
fun TransactionTypeItem(
    text : String,
    isSelected : Boolean,
    onClick : () -> Unit,
    modifier: Modifier
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Box(modifier = modifier
        .clickable { onClick() }
        .padding(8.dp)
        .clip(CircleShape)
        .background(color = color),
        contentAlignment = Alignment.Center){
        Text(
            text = text,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionPreview() {
    AddTranscationScreen(
        state = AddTransactionUiState(
            amount = 100.0,
            category = "Food",
            transactionType = TransactionType.expense,
            availableCategories = listOf("Food", "Shopping", "Transport"),
            selectedDate = LocalDateTime.now()
        ),
        onEvent = {}
    )
}