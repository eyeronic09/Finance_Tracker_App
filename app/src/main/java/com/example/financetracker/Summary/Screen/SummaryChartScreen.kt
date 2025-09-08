package com.example.financetracker.Summary.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financetracker.Summary.SummaryModel.TransactionChartViewModel
import com.example.financetracker.navigation.SealedScreen
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer

@Composable
fun SummaryChartScreen(navController: NavController,viewModel: TransactionChartViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val categoryTotals by viewModel.categoryTotalAndLable.collectAsState()
    val modelProducer = remember { CartesianChartModelProducer() }
    Scaffold(bottomBar = {
        NavigationBar {
            SealedScreen.bottomNavItems.forEach { screen ->
                NavigationBarItem(
                    selected = currentDestination?.route == screen.route,
                    onClick = {
                        navController.navigate(screen.route){
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState  = true
                        }
                    },
                    icon = {
                        val icon = if (currentDestination?.route == screen.route) {
                            screen.selectedIcon
                        } else {
                            screen.unselectedIcon
                        }
                        Icon(
                            imageVector = icon ?: Icons.Filled.Info,
                            contentDescription = screen.title
                        )
                    }
                )
            }
        }
    }) { innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)){
            Text("this chart Screen $categoryTotals , $modelProducer")
        }
    }

}