package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.financetracker.BudgetScreen.UI_Screen.BudgetTab
import com.example.financetracker.HomeScreen.HomeTab
import com.example.financetracker.SettingScreen.Ui.Screen.SettingTab
import com.example.financetracker.SettingScreen.Ui.Screen.ViewModel.SettingVM
import com.example.financetracker.ui.theme.FinanceTrackerTheme
import com.example.financetracker.ui.theme.LocalCurrency
import com.example.financetracker.ui.theme.getCurrencyInfo
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val viewModel: SettingVM = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            val darkTheme = when (uiState.themeSetting) {
                "Dark" -> true
                "Light" -> false
                else -> isSystemInDarkTheme()
            }

            val currencyInfo = getCurrencyInfo(uiState.selectedCurrency)

            FinanceTrackerTheme(darkTheme = darkTheme) {
                CompositionLocalProvider(LocalCurrency provides currencyInfo) {
                    TabNavigator(HomeTab) { _ ->
                        Scaffold(
                            contentWindowInsets = WindowInsets(0, 0, 0, 0),
                            bottomBar = {
                                NavigationBar {
                                    TabNavigationItem(HomeTab)
                                    TabNavigationItem(BudgetTab)
                                    TabNavigationItem(SettingTab)
                                }
                            },
                            content = { padding ->
                                Box(Modifier.padding(padding)) {
                                    CurrentTab()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current.key == tab.key,
            onClick = { tabNavigator.current = tab },
            icon = {
                tab.options.icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = tab.options.title
                    )
                }
            },
            label = { Text(tab.options.title) }
        )
    }
}
