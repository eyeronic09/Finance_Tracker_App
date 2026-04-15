package com.example.financetracker.AddTransaction

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.financetracker.AddTransaction.compontent.RowScrollDropdown
import com.example.financetracker.AddTransaction.compontent.TransactionTypeRadioButtons
import kotlinx.coroutines.flow.collectLatest
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
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    val event = viewModel::onEvent
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            snackbarHostState.showSnackbar(message)
        }
    }

    AddTranscationScreen(
        state = state,
        onEvent = event,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTranscationScreen(
    state : AddTransactionUiState ,
    onEvent: (AddTransactionEvent) -> Unit ,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.current
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navigator?.pop() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        AddTransactionContent(
            state = state,
            onEvent = onEvent,
            modifier = modifier.padding(paddingValues)
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
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)){
        
        Text(
            text = "How much?",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (state.amount == 0.0) "" else state.amount.toString(),
            onValueChange = { s: String ->
                val amount = s.toDoubleOrNull() ?: 0.0
                onEvent(AddTransactionEvent.amountChange(amount))
            },
            placeholder = { Text("0.0", fontSize = 32.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            singleLine = true
        )

        TransactionTypeRadioButtons(
            selectedRadioButton = state.transactionType,
            onClick = {
                onEvent(AddTransactionEvent.OnTransactionTypeChange(it))
            }
        )

        Text(
            text = "Category",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        RowScrollDropdown(
            selectedCategory = state.category,
            categories = state.availableCategories,
            onCategorySelected = {
                onEvent(AddTransactionEvent.OnCategoryChange(it))
            }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Add a note...") },
            value = state.transactionNote ?: "",
            onValueChange = {
                onEvent(AddTransactionEvent.OnTransactionNoteChange(it))
            },
            maxLines = 3
        )
        
        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = { onEvent(AddTransactionEvent.saveTransaction) },
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Save Transaction", style = MaterialTheme.typography.titleMedium)
        }
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
        onEvent = {},
        snackbarHostState = SnackbarHostState()
    )
}
