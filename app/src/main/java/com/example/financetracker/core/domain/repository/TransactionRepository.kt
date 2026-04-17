package com.example.financetracker.core.domain.repository

import com.example.financetracker.BudgetScreen.Domain.model.Budget
import com.example.financetracker.core.domain.model.Category
import com.example.financetracker.core.domain.model.Transaction
import java.time.LocalDateTime

interface TransactionRepository {
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()
    suspend fun getAlltheCategory(): List<Category>

    // Budget
    suspend fun getBudget(local: LocalDateTime): Double?
    suspend fun setBudget(budget: Budget , local : LocalDateTime)
    suspend fun minusfromBudget(amount: Double,local : LocalDateTime)
    suspend fun addtoBudget(amount: Double,local : LocalDateTime)

    suspend fun getAllTransactionsExpensesOfThisMonth() : Double?
}