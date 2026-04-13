package com.example.financetracker.HomeScreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.financetracker.HomeScreen.viewmodel.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

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
        Navigator(HomeScreenContainer())
    }
}

class HomeScreenContainer : Screen {
    @Composable
    override fun Content() {
        val viewModel: HomeScreenViewModel = koinViewModel()
        val navigator = LocalNavigator.current

        // Navigate to the actual HomeScreenRoute which should be defined in the UI file
        HomeScreenRoute(viewModel = viewModel)
    }
}

@Composable
fun HomeScreenRoute(viewModel: HomeScreenViewModel) {
    // This is a placeholder - the actual implementation should be in the HomeScreen UI file
    // For now, just show a simple screen
    Text("Home Screen - To be implemented")
}