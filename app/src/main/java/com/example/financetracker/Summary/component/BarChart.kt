package com.example.financetracker.Summary.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.LabelProperties

@Composable
fun BarChartScreen(categoryTotals: Map<String, Double>) {
    val chartData = categoryTotals.entries.map { (category, total) ->
        Bars(
            label = category,
            values = listOf(
                Bars.Data(
                    label = category,
                    value = total,
                    color = Brush.linearGradient(colors = listOf(Color.Green , Color.Red  ))  // pick whatever color logic you want
                )
            )
        )
    }

    ColumnChart(
        modifier = Modifier
            .fillMaxSize(),
        data = chartData,
        labelProperties = LabelProperties(
            textStyle = MaterialTheme.typography.labelSmall,
            enabled = true,
            builder = { modifier, label , _ , _  ->
                AssistChip(
                    onClick = {},
                    label = { Text(label) },
                    border = BorderStroke(
                        brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primaryContainer,
                          MaterialTheme.colorScheme.primaryContainer
                        )),
                        width = 2.dp
                    )
                )
            }
        ),
        barProperties = BarProperties(
            thickness = 20.dp,         // width of each bar
            spacing = 4.dp,             // spacing between Bars
            cornerRadius = Bars.Data.Radius.Circular(6.dp),  // rounded corners
            style = DrawStyle.Fill      // bar style
        ),
        animationSpec = tween(durationMillis = 3000)
    )
}

@Preview(showBackground = true)
@Composable
private fun prevs() {
    val data = mapOf(
        "Coffee" to 100.0,
        "Food" to 20.0,
        "Transportation" to 300.0,
        "Groceries" to 400.2,
        "Entertainment" to 500.4,
        "Shopping" to 6.0,
        "Miscellaneous" to 70.0
    )

    BarChartScreen(categoryTotals = data)

}