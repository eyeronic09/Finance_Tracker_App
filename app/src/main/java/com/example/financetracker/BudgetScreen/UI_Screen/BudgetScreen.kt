
package com.example.financetracker.BudgetScreen.UI_Screen

import android.icu.text.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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
import java.time.LocalDate
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
        state.amount?.let {
            BudgetCard(
                budget = it,
                spent = state.spend,
                remaining = state.remaining,
                startDate = state.startDate,
                endDate = state.endDate
            )
        }
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
    spent: Double?,
    remaining: Double?,
    startDate : LocalDateTime,
    endDate : LocalDateTime

) {
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
                text = "₹${budget}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                val dateTimeFormat = DateTimeFormatter.ofPattern("MM-yyyy")
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
                Text(
                    text = "ACTIVE PERIOD ${dateTimeFormat.format(startDate)} - ${dateTimeFormat.format(endDate)}",
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
                        text = "${spent}",
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
                        text = "${remaining}",
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
private fun BudgetCardPreview() {
    FinanceTrackerTheme {
        BudgetCard(
            budget = 10000.0,
            spent = 2500.0,
            remaining = 7500.0,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )
    }
}
