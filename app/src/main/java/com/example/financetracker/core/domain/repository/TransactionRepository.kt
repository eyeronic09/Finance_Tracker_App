package com.example.financetracker.core.domain.repository

import com.example.financetracker.core.domain.model.Transaction

interface TransactionRepository {
    suspend fun getAllTransactions(): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()
}