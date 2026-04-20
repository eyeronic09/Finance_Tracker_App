package com.example.financetracker.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.HomeScreen.component.BalanceCard
import com.example.financetracker.HomeScreen.component.CustomDatePicker
import com.example.financetracker.HomeScreen.component.TransactionDetail
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenEvent
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenUiState
import com.example.financetracker.core.domain.model.Transaction
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun WeekContent(
    state: HomeScreenUiState,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val time = state.todayDate

    val startDate: LocalDate = remember { time.minusDays(100) }
    val endDate = remember { time.plusDays(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val calendarState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = time,
        firstDayOfWeek = firstDayOfWeek
    )

    Column(modifier = modifier) {
            BalanceCard(
                todaysBalance = state.balance,
                totalIncome = state.totalIncome,
                totalExpense = state.totalExpense,
                TotalBalances = state.TotalBalances
            )

        WeekCalendar(
            modifier = Modifier.padding( 4.dp),
            weekHeader = {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween ,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "Timeline",
                        style = MaterialTheme.typography.titleMedium
                    )
                    TextButton(onClick = {
                        onEvent(HomeScreenEvent.OpenDatePicker)
                    }) {
                        Text(text = "View Calendar")
                    }
                }

            },
            state = calendarState,
            dayContent = { day ->
                val date = day.date
                val isSelected = date == state.todayDate
                val color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else  {
                    Color.Transparent
                }
                val colorText = if (isSelected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurface
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color = color)
                        .clickable { onEvent(HomeScreenEvent.OnDateSelected(date)) }
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = date.dayOfWeek.name.take(3),
                        style = MaterialTheme.typography.labelSmall,
                        color = colorText
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = colorText
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(state.transactions) { transaction ->
                TransactionDetail(
                    transaction = transaction,
                    onClick = { onEvent(HomeScreenEvent.SelectTransaction(transaction)) }
                )
            }
        }
    }
    if(state.openDatePicker){
        CustomDatePicker(
            onDismiss = { onEvent(HomeScreenEvent.OpenDatePicker) },
            onDateSelected = { millis ->
                millis?.let { it ->
                    val date = Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onEvent(HomeScreenEvent.OnDateSelected(date))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeekContentPreview() {
    val mockTransactions = listOf(
        Transaction(
            id = 1,
            amount = 500.0,
            type = "Income",
            category = "Salary",
            date = LocalDateTime.now(),
            note = "Monthly salary"
        ),
        Transaction(
            id = 2,
            amount = 50.0,
            type = "Expense",
            category = "Food",
            date = LocalDateTime.now(),
            note = "Lunch"
        )
    )

    val mockState = HomeScreenUiState(
        transactions = mockTransactions,
        todayDate = LocalDate.now(),
        totalIncome = 500.0,
        totalExpense = 50.0,
        balance = 450.0
    )

    WeekContent(
        state = mockState,
        onEvent = {}
    )
}