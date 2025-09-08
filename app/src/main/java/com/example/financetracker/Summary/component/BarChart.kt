package com.example.financetracker.Summary.component


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer


@Composable
fun BarChart(modelProducer: CartesianChartModelProducer , modifier: Modifier ){
    CartesianChartHost(
        chart = rememberCartesianChart(startAxis = VerticalAxis.rememberStart(), bottomAxis = HorizontalAxis.rememberBottom()),
        modelProducer = modelProducer
    )
}
