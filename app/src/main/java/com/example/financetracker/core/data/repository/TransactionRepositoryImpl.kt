package com.example.financetracker.core.data.repository

import com.example.financetracker.core.data.local.dao.TransactionDao
import com.example.financetracker.core.data.local.mapper.toEntity
import com.example.financetracker.core.data.local.mapper.toDomain
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.core.domain.repository.TransactionRepository

class TransactionRepositoryImpl(private val transactionDao: TransactionDao) :
    TransactionRepository {
    
    override suspend fun getAllTransactions(): List<Transaction> {
        return transactionDao.getAllTransactions().map { it.toDomain(it.category) }
    }
    
    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insert(transaction.toEntity())
    }
    
    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }
    
    override suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.delete(transaction.toEntity())
    }
    
    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }
}