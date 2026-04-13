package com.example.financetracker.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Int = 0,
    val amount: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
