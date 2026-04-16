package com.example.financetracker.HomeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenUiState
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate


@Composable
fun WeekContent(state : HomeScreenUiState , modifier: Modifier = Modifier) {
    val time  = state.todayDate

    val startDate: LocalDate = remember { time.minusDays(100) as LocalDate }
    val endDate = remember { time.plusDays(100) }

    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }


    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = time,
        firstDayOfWeek = firstDayOfWeek
    )
    WeekCalendar(
        state = state,
        dayContent = { day ->

        }
    )

}