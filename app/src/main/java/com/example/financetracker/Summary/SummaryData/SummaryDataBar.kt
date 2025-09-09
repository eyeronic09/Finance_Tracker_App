package com.example.financetracker.Summary.SummaryData

import co.yml.charts.axis.AxisData

data class SummaryDataBar(
    val chartData: Map<String , Double>,
    val xAxisData: AxisData = AxisData.Builder().build(),
    val yAxisData: AxisData = AxisData.Builder().build(),
)