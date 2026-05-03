package com.example.financetracker.BudgetScreen.UI_Screen

import androidx.appcompat.widget.ListPopupWindow.MATCH_PARENT
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
import java.util.*

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

}
