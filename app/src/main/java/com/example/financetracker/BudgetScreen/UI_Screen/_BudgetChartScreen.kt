package com.example.financetracker.BudgetScreen.UI_Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.financetracker.BudgetScreen.Domain.model.CategoryBudget
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import org.koin.androidx.compose.koinViewModel

class _BudgetChartScreen : Screen {
    @Composable
    override fun Content() {
        BudgetChartRoute()
    }
}

@Composable
fun BudgetChartRoute(viewModel: BudgetChartVM = koinViewModel()) {
    val state by viewModel.CategoryBudgetUiState.collectAsState()
    val navigator = LocalNavigator.current

    BudgetChartScreen(
        state = state,
        onBackClick = { navigator?.pop() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetChartScreen(
    state: CategoryBudgetUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spending Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Monthly Spending by Category",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.categoryBudget.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No expenses to display")
                }
            } else {
                BudgetPieChart(state.categoryBudget)
            }
        }
    }
}

@Composable
fun BudgetPieChart(categoryBudgets: List<CategoryBudget>) {
    val pieData = categoryBudgets.map { category ->
        Pie(
            label = category.categoryName,
            data = category.sum,
            color = getCategoryColor(category.categoryName),
            selectedColor = getCategoryColor(category.categoryName).copy(alpha = 0.8f)
        )
    }

    Box(
        modifier = Modifier
            .size(300.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        PieChart(
            modifier = Modifier.fillMaxSize(),
            data = pieData
        )
    }
}

// Helper to provide consistent colors for categories
fun getCategoryColor(categoryName: String): Color {
    return when (categoryName.lowercase()) {
        "food" -> Color(0xFFFF9800)
        "shopping" -> Color(0xFFE91E63)
        "transportation" -> Color(0xFF2196F3)
        "movies" -> Color(0xFF9C27B0)
        "rent" -> Color(0xFF795548)
        "bills & utilities" -> Color(0xFF607D8B)
        "healthcare" -> Color(0xFFF44336)
        "investment" -> Color(0xFF4CAF50)
        "salary" -> Color(0xFF8BC34A)
        else -> Color.Gray
    }
}
