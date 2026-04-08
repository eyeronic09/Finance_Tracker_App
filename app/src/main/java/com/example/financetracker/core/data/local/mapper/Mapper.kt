package com.example.financetracker.core.data.local.mapper

import com.example.financetracker.core.data.local.entity.TransactionEntity
import com.example.financetracker.core.data.local.entity.CategoryEntity
import com.example.financetracker.core.data.local.entity.BudgetEntity
import com.example.financetracker.core.domain.model.Transaction

fun TransactionEntity.toDomain(categoryName: String): Transaction {
    return Transaction(
        id = transactionId,
        amount = amount,
        type = type,
        category = categoryName,
        date = date,
        note = note
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = id,
        amount = amount,
        type = type,
        categoryId = 0,
        date = date,
        note = note
    )
}