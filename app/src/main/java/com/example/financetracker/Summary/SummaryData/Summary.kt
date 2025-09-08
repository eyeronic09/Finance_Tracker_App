package com.example.financetracker.Summary.SummaryData

data class Summary(
    val category: String,
    val income: Double,
    val expense: Double,
    val netBalance: Double
)