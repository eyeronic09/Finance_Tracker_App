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
    sealed class BottomNavScreen(
        route: String,
        title: String,
        selectedIcon: ImageVector,
        unselectedIcon: ImageVector
    ) : SealedScreen(route, title, selectedIcon, unselectedIcon)

    object HomeScreen : SealedScreen("HomeScreen")
    object AddScreen : SealedScreen("AddScreen")

    object EditScreen : SealedScreen("EditScreen")

    object SummaryScreen : BottomNavScreen(
        route = "SummaryScreen",
        title = "Summary",
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List
    )

    object SummaryChart : BottomNavScreen(
        route = "SummaryChart",
        title = "Charts",
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart
    )

    companion object {
        val bottomNavItems = listOf(SummaryScreen, SummaryChart)
    }
}
