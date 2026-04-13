package com.example.financetracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetracker.core.data.local.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE transactionId = :id")
    suspend fun getById(id: Int): TransactionEntity?

    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND strftime('%Y-%m', dateAndTime) = strftime('%Y-%m', 'now')")
    suspend fun getAllTransactionsExpensesOfThisMonth(): Double?
}
