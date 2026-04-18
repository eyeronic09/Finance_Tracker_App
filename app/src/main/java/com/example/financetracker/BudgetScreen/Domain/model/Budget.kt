package com.example.financetracker.BudgetScreen.Domain.model

import java.time.LocalDateTime

data class Budget(
    val budgetId: Int,
    val amount: Double,
    val startDate: LocalDateTime,
    val endDate : LocalDateTime
)