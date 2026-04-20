package com.example.financetracker.BudgetScreen.UI_Screen

import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import com.example.financetracker.BudgetScreen.Domain.model.CategoryBudget
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
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
    
    BudgetChartScreen(state = state)
}

@Composable
fun BudgetChartScreen(state: CategoryBudgetUiState) {
    MPPieChart(state.categoryBudget)
}

@Composable
fun MPPieChart(state: List<CategoryBudget>) {
    AndroidView(
        factory = { context ->
           com.github.mikephil.charting.charts.PieChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT
                )
                description.isEnabled = false // Disable default description
                isDrawHoleEnabled = true      // Set to false for a solid pie
                setHoleColor(Color.TRANSPARENT)
            }
        },
        modifier = Modifier.fillMaxSize().padding(16.dp),
        update = { pieChart ->
            val entries = state.map { PieEntry(it.sum.toFloat() , it.categoryName) }
            val dataSet = PieDataSet(entries , "Category").apply {
                colors = ColorTemplate.MATERIAL_COLORS.toList() // Use built-in or custom colors
                valueTextColor = Color.BLACK
                valueTextSize = 14f
            }

            // 4. Bind data to chart and refresh
            pieChart.data = PieData(dataSet)
            pieChart.invalidate() // Essential to trigger a redraw
        }
    )
}
