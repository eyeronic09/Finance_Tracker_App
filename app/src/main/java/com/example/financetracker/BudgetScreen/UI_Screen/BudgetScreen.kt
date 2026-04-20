package com.example.financetracker.BudgetScreen.UI_Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.financetracker.BudgetScreen.compontent.BudgetCard
import com.example.financetracker.BudgetScreen.compontent.CategoryBudgetCard
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
            icon = painterResource(id = R.drawable.outline_money_24)
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
    ) { paddingValues ->
        BudgetScreenContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.padding(paddingValues)
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
    state: BudgetUiState,
    onEvent: (BudgetEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
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

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This Months expenses",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = modifier.clickable(onClick = { navigator?.push(_BudgetChartScreen()) }),
                    text = "VIEW ALL",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(state.listOfCategory) { category ->
            CategoryBudgetCard(categoryBudget = category)
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
