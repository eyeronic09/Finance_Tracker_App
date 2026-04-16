package com.example.financetracker.HomeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenEvent
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenUiState
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel
import com.example.financetracker.AddTransaction._AddTranscationScreen
import com.example.financetracker.HomeScreen.component.CustomDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "Home",
            icon = rememberVectorPainter(Icons.Default.Home)
        )

    @Composable
    override fun Content() {
        Navigator(_HomeScreen())
    }
}

class _HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreenRoute()
    }
}

@Composable
fun HomeScreenRoute(viewModel: HomeScreenViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val event = viewModel::onEvent
    val navigator = LocalNavigator.current

    navigator?.let {
        HomeScreen(
            state = state,
            onAction = event,
            navigator = it,
        )
    }
}

@Composable
fun HomeScreen(
    state: HomeScreenUiState,
    onAction: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
    navigator: Navigator
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.push(_AddTranscationScreen())
                },
                content = { Text("+") }
            )
        },
    ) { it ->
        HomeScreenContent(
            state = state,
            onAction = onAction,
            modifier = modifier.padding(it)
        )
    }
}

@Composable
fun HomeScreenContent(
    state: HomeScreenUiState,
    onAction: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.openDatePicker) {
        CustomDatePicker(
            onDismiss = { onAction(HomeScreenEvent.OpenDatePicker) },
            onDateSelected = { millis ->
                millis?.let {
                    val date = Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onAction(HomeScreenEvent.OnDateSelected(date))
                }
            }
        )
    }

    WeekContent(
        state = state,
        onEvent = onAction,
        modifier = modifier.fillMaxSize()
    )
}