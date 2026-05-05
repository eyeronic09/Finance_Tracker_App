package com.example.financetracker.core.domain.model

import java.time.LocalDateTime

data class Goal(
    val id: Int = 0,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val deadline: LocalDateTime? = null
) {
    val progress: Float
        get() = if (targetAmount > 0) (savedAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f
    
    val remainingAmount: Double
        get() = (targetAmount - savedAmount).coerceAtLeast(0.0)
    
    val isCompleted: Boolean
        get() = savedAmount >= targetAmount
}
