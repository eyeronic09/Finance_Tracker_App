package com.example.financetracker.core.domain.model

import com.example.financetracker.AddTransaction.TransactionType

data class Category(
    val id: Int = 0 ,
    val name: String,
    val type: TransactionType
)