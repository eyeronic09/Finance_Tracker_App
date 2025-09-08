package com.example.financetracker.navigation

import kotlinx.serialization.Serializable

// SealedScreen.kt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SealedScreen(
    val route: String,
    val title: String = "",
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    object HomeScreen : SealedScreen(
        route = "HomeScreen",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object AddScreen : SealedScreen(
        route = "AddScreen",
        title = "Add Transaction",
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List
    )

    object SummaryScreen : SealedScreen(
        route = "SummaryScreen",
        title = "Summary",
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List
    )

    object SummaryChart : SealedScreen(
        route = "SummaryChart",
        title = "Charts",
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    )
    //for summary screen
    companion object {
        // List of all bottom navigation items
        val bottomNavItems = listOf(SummaryScreen, SummaryChart)
    }
}