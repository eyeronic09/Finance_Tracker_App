package com.example.financetracker.core.domain.repository

import com.example.financetracker.BudgetScreen.Domain.model.Budget
import com.example.financetracker.core.domain.model.Transaction

interface TransactionRepository {
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun getAllExpensesTransactionsOfThisMonths(): Double
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()

    // Budget
    suspend fun getBudget(): Double
    suspend fun setBudget(budget: Budget)
    suspend fun minusfromBudget(amount: Double)
    suspend fun addtoBudget(amount: Double)
}