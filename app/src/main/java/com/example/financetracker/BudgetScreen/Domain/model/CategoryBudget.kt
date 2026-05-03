package com.example.financetracker.BudgetScreen.Domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryBudget(
    val icon: ImageVector,
    val categoryName: String,
    val sum: Double,
    val limit : Double = 0.0

) {
    val remaining: Double get() = limit - sum
    val progress: Float get() = if (limit > 0) (sum / limit).coerceAtMost(maximumValue = 1.0).toFloat() else 0f
    val isOverBudget: Boolean get() = sum > limit
}
