package com.example.financetracker.Summary.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Line

@Composable
fun barChartScreen(categoryTotals: Map<String, Double>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val data = categoryTotals.map { (string, amount) ->
            Line(
                label = string,
                values = listOf(amount),
                color = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF23af92),
                        Color(0xFFFF0000)

                    )
                ),
            )
        }
        LineChart(
            data = data,
            modifier = TODO(),
            curvedEdges = TODO(),
            animationDelay = TODO(),
            animationMode = TODO(),
            dividerProperties = TODO(),
            gridProperties = TODO(),
            zeroLineProperties = TODO(),
            indicatorProperties = TODO(),
            labelHelperProperties = TODO(),
            labelHelperPadding = TODO(),
            textMeasurer = TODO(),
            popupProperties = TODO(),
            dotsProperties = TODO(),
            labelProperties = TODO(),
            maxValue = TODO(),
            minValue = TODO(),
        )
    }
}