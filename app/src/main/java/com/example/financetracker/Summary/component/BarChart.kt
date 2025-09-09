package com.example.financetracker.Summary.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData


@Composable
fun BarChart(categoryTotals: Map<String, Double>) {
    val barDataList = categoryTotals.map { (label, amount) ->
        BarData(
            point = Point(x = 0f, y = amount.toFloat()), // X is placeholder (handled by axis steps)
            label = label,

        )
    }
    val xAxisData = AxisData.Builder().axisStepSize(40.dp).labelData { index ->
        barDataList[index].label
    }.steps(barDataList.size - 1).build()
    val maxAmount = (categoryTotals.values.maxOrNull() ?: 0.0).toFloat()
    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelData { step -> (step * (maxAmount / 5)).toInt().toString() }
        .build()

    // 4. Assemble BarChartData
    val barChartData = BarChartData(
        chartData = barDataList,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
    )

    // 5. Render chart
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        barChartData = barChartData
    )

}