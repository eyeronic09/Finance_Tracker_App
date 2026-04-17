package com.example.financetracker.core.data.local.mapper

import com.example.financetracker.AddTransaction.TransactionType
import com.example.financetracker.BudgetScreen.Domain.model.Budget
import com.example.financetracker.core.data.local.entity.BudgetEntity
import com.example.financetracker.core.data.local.entity.CategoryEntity
import com.example.financetracker.core.data.local.entity.TransactionEntity
import com.example.financetracker.core.domain.model.Category
import com.example.financetracker.core.domain.model.Transaction

fun TransactionEntity.toDomain(categoryName: String): Transaction {
    return Transaction(
        id = transactionId,
        amount = amount,
        type = type,
        category = categoryName,
        date = dateAndTime,
        note = note
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        transactionId = id,
        amount = amount,
        type = type,
        categoryId = 0,
        dateAndTime = date,
        note = note
    )
}

fun BudgetEntity.toDomain(): Budget {
    return Budget(
        budgetId = budgetId,
        amount = amount,
        startDate = startDate,
    )

}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        budgetId = budgetId,
        amount = amount,
        startDate = startDate,

    )
}

fun Category.toEntity(): CategoryEntity{
    return CategoryEntity(
        categoryId = id,
        name = name,
        type = type.name,
    )
}

fun CategoryEntity.toDomain(): Category{
    return Category(
        id = categoryId,
        name = name,
        type = TransactionType.fromString(type)
    )
}