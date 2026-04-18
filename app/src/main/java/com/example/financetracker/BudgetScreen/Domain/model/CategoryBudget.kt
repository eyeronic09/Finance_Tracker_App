package com.example.financetracker.BudgetScreen.Domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryBudget(
    val icon: ImageVector,
    val categoryName : String,
    val sum : Double
)