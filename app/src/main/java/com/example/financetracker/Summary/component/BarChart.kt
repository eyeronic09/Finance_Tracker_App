package com.example.financetracker.Summary.component

import android.R
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financetracker.ui.theme.Typography
import com.google.android.material.color.MaterialColors
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties

@Composable
fun BarChartScreen(categoryTotals: Map<String, Double>) {
    val chartData = categoryTotals.entries.map { (category, total) ->
        Bars(
            label = category,
            values = listOf(
                Bars.Data(
                    value = total,
                    color = Brush.linearGradient(colors = listOf(Color.Green , Color.Red  ))

                ),
            )
        )
    }

    ColumnChart(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        data = chartData,
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground
            )
        ),
        labelProperties = LabelProperties(
            textStyle = TextStyle(
               color = Color.Red
            ),
            enabled = true,
            builder = { modifier, label , value , _  ->
                AssistChip(
                    onClick = {},
                    label = { Text(label ) },
                    border = BorderStroke(
                        brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primaryContainer,
                          MaterialTheme.colorScheme.primaryContainer
                        )),
                        width = 2.dp
                    ),

                )
            }

        ),
        barProperties = BarProperties(
            thickness = 20.dp,
            spacing = 4.dp,
            style = DrawStyle.Fill ,

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