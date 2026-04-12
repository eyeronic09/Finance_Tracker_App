package com.example.financetracker.core.data.repository

import com.example.financetracker.core.data.local.dao.TransactionDao
import com.example.financetracker.core.data.local.dao.CategoryDao
import com.example.financetracker.core.data.local.dao.BudgetDao
import com.example.financetracker.core.data.local.mapper.toEntity
import com.example.financetracker.core.data.local.mapper.toDomain
import com.example.financetracker.core.data.local.entity.CategoryEntity
import com.example.financetracker.core.data.local.entity.BudgetEntity
import com.example.financetracker.BudgetScreen.Domain.model.Budget
import com.example.financetracker.core.domain.model.Transaction
import com.example.financetracker.core.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val budgetDao: BudgetDao
) : TransactionRepository {

    /**
     * This method returns all transactions from a specific category.
     *
     * @return A list of [Transaction] objects, each representing a transaction from the specified category.
     * The list is obtained by querying the database for all transaction entities and then mapping each entity
     * to a domain object. The domain object is created by finding the corresponding category entity in the database
     * and passing its name to the [TransactionEntity.toDomain] function. If the category entity is not found,
     * the category name is set to "Unknown".
     */
    override suspend fun getAllTransactions(): List<Transaction> {
        return transactionDao.getAllTransactions().map { transactionEntity ->
            val category = categoryDao.getById(transactionEntity.categoryId)
            // If the category entity is not found, set the category name to "Unknown"
            transactionEntity.toDomain(category?.name ?: "Unknown")
        }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        // Find or create category
        val categories = categoryDao.getAll()
        val categoryEntity = categories.find { it.name == transaction.category }
            ?: CategoryEntity(name = transaction.category, type = transaction.type).also {
                categoryDao.insert(it)
            }

        val transactionEntity = transaction.toEntity().copy(
            categoryId = categoryEntity.categoryId
        )
        transactionDao.insert(transactionEntity)
    }

    override suspend fun updateTransaction(transaction: Transaction) {

        val categories = categoryDao.getAll()
        val categoryEntity = categories.find { it.name == transaction.category }
            ?: CategoryEntity(name = transaction.category, type = transaction.type).also {
                categoryDao.insert(it)
            }

        val transactionEntity = transaction.toEntity().copy(
            categoryId = categoryEntity.categoryId
        )
        transactionDao.update(transactionEntity)
    }

    override suspend fun getAllExpensesTransactionsOfThisMonths(): Double {
        return transactionDao.getAllTransactionsExpensesOfThisMonth() ?: 0.0
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        // Find the transaction by ID to get the entity for deletion
        val transactions = transactionDao.getAllTransactions()
        val entityToDelete = transactions.find { it.transactionId == transaction.id }
        entityToDelete?.let { transactionDao.delete(it) }
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }

    override suspend fun getBudget(): Double {
        return budgetDao.getBudget()?.amount ?: 0.0
    }

    override suspend fun setBudget(budget: Budget) {
        budgetDao.clear()
        budgetDao.insert(budget.toEntity())
    }

    override suspend fun minusfromBudget(amount: Double) {
        val current =  budgetDao.getBudgetAmount()
        val total = current?.minus(amount)
        if (total != null) {
            budgetDao.updateBudgetAmount(total)
        }


    }

    override suspend fun addtoBudget(amount: Double) {
        val current =  budgetDao.getBudgetAmount()
        val total = current?.plus(amount)
        if (total != null) {
            budgetDao.updateBudgetAmount(total)
        }

    }


}