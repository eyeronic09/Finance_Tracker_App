package com.example.financetracker.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.HomeScreen.component.TransactionDetail
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenEvent
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenUiState
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import androidx.compose.ui.tooling.preview.Preview
import com.example.financetracker.core.domain.model.Transaction
import java.time.LocalDate
import java.time.LocalDateTime

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
        WeekCalendar(
            weekHeader = { week ->
                val firstDate = week.days.first().date
                val monthName = firstDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
                Text(
                    text = "$monthName ${firstDate.year}",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp),
                    fontWeight = FontWeight.SemiBold
                )
            },
            state = calendarState,
            dayContent = { day ->
                val date = day.date
                val isSelected = date == state.todayDate
                val isToday = date == LocalDate.now()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { onEvent(HomeScreenEvent.OnDateSelected(date)) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = date.dayOfWeek.name.take(3))
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isSelected) Color.White else if (isToday) Color.Blue else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                        modifier = if (isSelected) {
                            Modifier
                                .background(Color.Blue, shape = CircleShape)
                                .padding(4.dp)
                        } else Modifier
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