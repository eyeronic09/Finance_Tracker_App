package com.example.financetracker.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.financetracker.core.domain.model.Goal
import com.example.financetracker.ui.theme.LocalCurrency
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat

object GoalsTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = "Goals",
            icon =  rememberVectorPainter(Icons.Default.Flag)
        )

    @Composable
    override fun Content() {
        Navigator(_GoalScreen())
    }
}

class _GoalScreen : Screen {
    @Composable
    override fun Content() {
        GoalScreenRoute()
    }
}

@Composable
fun GoalScreenRoute(viewModel: GoalViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val event = viewModel::onEvent

    GoalScreen(
        state = state,
        onEvent = event
    )
}

@Composable
fun GoalScreen(
    state: GoalUiState,
    onEvent: (GoalEvent) -> Unit
) {
    val currencyInfo = LocalCurrency.current
    val currencyFormat = remember(currencyInfo) {
        NumberFormat.getCurrencyInstance(currencyInfo.locale).apply {
            maximumFractionDigits = 0
        }
    }

    Scaffold(
        topBar = {
            Text(
                text = "Savings Goals",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(GoalEvent.ShowAddGoalDialog) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { paddingValues ->
        if (state.goals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No goals set yet. Start saving!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.goals) { goal ->
                    GoalCard(
                        goal = goal,
                        currencyFormat = currencyFormat,
                        onAddFunds = { onEvent(GoalEvent.ShowAddFundsDialog(goal)) },
                        onDelete = { onEvent(GoalEvent.DeleteGoal(goal)) }
                    )
                }
            }
        }

        if (state.isAddGoalDialogVisible) {
            AddGoalDialog(
                name = state.newGoalName,
                target = state.newGoalTarget,
                onNameChange = { onEvent(GoalEvent.OnNameChange(it)) },
                onTargetChange = { onEvent(GoalEvent.OnTargetChange(it)) },
                onDismiss = { onEvent(GoalEvent.DismissAddGoalDialog) },
                onConfirm = { onEvent(GoalEvent.SaveGoal) }
            )
        }

        if (state.isAddFundsDialogVisible) {
            AddFundsDialog(
                goalName = state.selectedGoal?.name ?: "",
                amount = state.fundAmount,
                onAmountChange = { onEvent(GoalEvent.OnFundAmountChange(it)) },
                onDismiss = { onEvent(GoalEvent.DismissAddFundsDialog) },
                onConfirm = { onEvent(GoalEvent.AddFunds) }
            )
        }
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    currencyFormat: NumberFormat,
    onAddFunds: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = goal.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { goal.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = if (goal.isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(goal.progress * 100).toInt()}% completed",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${currencyFormat.format(goal.savedAmount)} / ${currencyFormat.format(goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!goal.isCompleted) {
                Button(
                    onClick = onAddFunds,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text("Add Funds")
                }
            } else {
                Text(
                    text = "Goal Achieved! 🎉",
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AddGoalDialog(
    name: String,
    target: Double,
    onNameChange: (String) -> Unit,
    onTargetChange: (Double) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Saving Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Goal Name (e.g. New Phone)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = if (target == 0.0) "" else target.toString(),
                    onValueChange = { onTargetChange(it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Target Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    prefix = { Text(LocalCurrency.current.symbol) }
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, enabled = name.isNotBlank() && target > 0) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddFundsDialog(
    goalName: String,
    amount: Double,
    onAmountChange: (Double) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Funds to $goalName") },
        text = {
            OutlinedTextField(
                value = if (amount == 0.0) "" else amount.toString(),
                onValueChange = { onAmountChange(it.toDoubleOrNull() ?: 0.0) },
                label = { Text("Amount to Save") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                prefix = { Text(LocalCurrency.current.symbol) }
            )
        },
        confirmButton = {
            Button(onClick = onConfirm, enabled = amount > 0) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
