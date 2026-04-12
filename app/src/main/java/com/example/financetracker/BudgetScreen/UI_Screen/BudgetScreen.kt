
package com.example.financetracker.BudgetScreen.UI_Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.financetracker.R
import com.example.financetracker.ui.theme.FinanceTrackerTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object BudgetTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
                index = 1u,
                title = "Budget",
                icon = painterResource(
                    id = R.drawable.outline_money_24,
            )
        )

    @Composable
    override fun Content() {
        Navigator(_BudgetScreen())
    }
}

class _BudgetScreen : Screen {
    @Composable
    override fun Content() {
        BudgetScreenRoute()
    }
}

@Composable
fun BudgetScreenRoute(viewModel: BudgetViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val event = viewModel::onEvent
    val navigator = LocalNavigator.current

    BudgetScreen(
        state = state,
        onEvent = event
    )
}





@Composable
fun BudgetScreen(
    state: BudgetUiState,
    onEvent: (BudgetEvent) -> Unit
) {
    Scaffold(
       floatingActionButton = {
           FloatingActionButton(
               onClick = { onEvent(BudgetEvent.ShowAddBudgetDialog(true)) },
               content = { Text("+") }
           )
       }
    ) { it ->
        BudgetScreenContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.padding(it)
        )
    }

    if (state.isAddBudgetDialogVisible) {
        AddBudgetFabScreen(
            amount = state.newBudgetAmount,
            onAmountChange = { onEvent(BudgetEvent.OnAmountChange(it)) },
            onDismiss = { onEvent(BudgetEvent.ShowAddBudgetDialog(false)) },
            onConfirm = { onEvent(BudgetEvent.AddBudget) }
        )
    }
}

@Composable
fun BudgetScreenContent(
    state: BudgetUiState ,
    onEvent: (BudgetEvent) -> Unit ,
    modifier: Modifier = Modifier)  {
    Column(modifier = modifier.fillMaxSize()) {
        BudgetCard(
            budget = state.amount,
            start = state.startDate,
            end = state.endDate,
            spent = state.spent,
            remaining = state.remaining
        )
    }
}

//@Preview(showBackground = true)
@Composable
private fun BudgetScreenContentPreview() {
    BudgetScreenContent(
        state = BudgetUiState(amount = 5000.0),
        onEvent = {}
    )
}


@Composable
fun BudgetCard(
    budget: Double,
    start : LocalDateTime,
    end : LocalDateTime,
    spent : Double,
    remaining : Double
) {
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy")


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MONTHLY BUDGET",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = formatter.format(start) + " - " + formatter.format(end),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green,
                modifier = Modifier
                    .border(1.dp, Color.Green, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )

            Text(
                text = "₹${budget}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
                Text(
                    text = "ACTIVE PERIOD",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SPENT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$spent",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "REMAINING",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$remaining",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetCardPreview() {
    FinanceTrackerTheme {
        BudgetCard(
            budget = 5000.0,
            start = LocalDateTime.now(),
            end = LocalDateTime.now().plusMonths(1),
            spent = 2000.0,
            remaining = 3000.0
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetScreenPreview() {
    FinanceTrackerTheme {
        BudgetScreen(
            state = BudgetUiState(
                amount = 5000.0,
                isAddBudgetDialogVisible = false,
                newBudgetAmount = 0.0,
                spent = 2000.0,
                remaining = 3000.0

            ),
            onEvent = {}
        )
    }
}
