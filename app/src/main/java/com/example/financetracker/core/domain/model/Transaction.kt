package com.example.financetracker.core.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Int,
    val amount: Double,
    val type: String,
    val category: String,
    val date: LocalDateTime,
    val note: String
)