package com.example.financetracker.AddTransaction.compontent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

fun getCategoryIcon(category: String): ImageVector {
    return when (category) {
        "Food" -> Icons.Default.FoodBank
        "Food & Dining" -> Icons.Default.FoodBank
        "Shopping" -> Icons.Default.ShoppingCart
        "Transportation" -> Icons.Default.DirectionsCar
        "Movies" -> Icons.Default.LocalMovies
        "Salary" -> Icons.Default.AttachMoney
        "Investment" -> Icons.Default.TrendingUp
        "Rent" -> Icons.Default.Home
        "Bills & Utilities" -> Icons.Default.Receipt
        "Healthcare" -> Icons.Default.MedicalServices
        "Other Income" -> Icons.Default.AccountBalance
        "Other Expense" -> Icons.Default.Money
        else -> Icons.Default.Help
    }
}