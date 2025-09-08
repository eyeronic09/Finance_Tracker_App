package com.example.financetracker.Summary.Screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financetracker.Summary.SummaryModel.SummaryViewModel
import com.example.financetracker.Summary.component.SummaryCard
import com.example.financetracker.navigation.SealedScreen
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryScreen(
    navController: NavController,
    viewModel: SummaryViewModel
) {

    val dates = listOf("last7", "last30", "custom")
    val summaryList by viewModel.summaryFlow.collectAsState()
    val filteredDate by viewModel.filterDate.collectAsState()
    Log.d("FilterDate" , filteredDate.toString())
    val selectedDateRangeFilter by viewModel.selectedDateRange.collectAsState()
    val customStartDate by viewModel.customStartDate.collectAsState()
    val customEndDate by viewModel.customEndDate.collectAsState()
    
    // State for date picker dialog
    val showDateRangePicker = remember { mutableStateOf(false) }
    
    // Convert LocalDate to milliseconds for DatePicker
    val startDateMillis = customStartDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    val endDateMillis = customEndDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    
    val configuration = LocalConfiguration.current
    val dateRangePickerState = remember {
        DateRangePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedStartDateMillis = startDateMillis,
            initialSelectedEndDateMillis = endDateMillis,
            yearRange = 2010..2030,
            locale = configuration.locale
        )
    }
    // Obtain the current back stack entry and destination from the NavController
    // This is used to determine if the current screen is the selected destination
    // in the bottom navigation bar.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Summary") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                SealedScreen.bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dates) { chip ->
                        val isSelected = selectedDateRangeFilter == chip
                        Log.d("DateFilter", "Chip: $chip, Selected: $isSelected")
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (chip == "custom") {
                                    showDateRangePicker.value = true
                                } else {
                                    viewModel.selectedDateRange(chip)
                                }
                            },
                            label = {
                                Text(
                                    when (chip) {
                                        "last7" -> "Last 7 Days"
                                        "last30" -> "Last 30 Days"
                                        "custom" -> "Custom"
                                        else -> chip
                                    },
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = if (isSelected) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                        )
                    }
                }
                
                // Display selected date range when custom is selected
                if (selectedDateRangeFilter == "custom" && customStartDate != null && customEndDate != null) {
                    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                    Text(
                        text = "${customStartDate!!.format(formatter)} - ${customEndDate!!.format(formatter)}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }

            items(summaryList.filter { it.income > 0 || it.expense > 0 }) { summary ->
                SummaryCard(summary = summary)
            }
        }

        // Date Range Picker Dialog
        if (showDateRangePicker.value) {
            val dateFormatter = DateTimeFormatter.ofPattern("MMM , dd, yyyy")
            
            DatePickerDialog(
                onDismissRequest = { showDateRangePicker.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val startDate = dateRangePickerState.selectedStartDateMillis?.let { millis ->
                                Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            }
                            val endDate = dateRangePickerState.selectedEndDateMillis?.let { millis ->
                                Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            }
                            
                            if (startDate != null && endDate != null) {
                                viewModel.setCustomDateRange(startDate, endDate)
                                viewModel.selectedDateRange("custom")
                            }
                            showDateRangePicker.value = false
                        },
                        enabled = dateRangePickerState.selectedStartDateMillis != null && 
                                 dateRangePickerState.selectedEndDateMillis != null
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDateRangePicker.value = false }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DateRangePicker(
                    state = dateRangePickerState,
                    title = {
                        Text("Select Date Range")
                    },
                    headline = {
                        val startDate = dateRangePickerState.selectedStartDateMillis?.let { millis ->
                            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                                .format(dateFormatter)
                        } ?: "Start date"
                        
                        val endDate = dateRangePickerState.selectedEndDateMillis?.let { millis ->
                            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                                .format(dateFormatter)
                        } ?: "End date"
                        
                        Text("$startDate - $endDate")
                    }
                )
            }
        }

    }
}
