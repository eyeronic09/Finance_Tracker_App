package com.example.financetracker.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.financetracker.core.data.local.dao.BudgetDao
import com.example.financetracker.core.data.local.dao.CategoryDao
import com.example.financetracker.core.data.local.dao.TransactionDao
import com.example.financetracker.core.data.local.entity.BudgetEntity
import com.example.financetracker.core.data.local.entity.CategoryEntity
import com.example.financetracker.core.data.local.entity.TransactionEntity
import com.example.financetracker.core.data.local.typeconverter.TypeConverter

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
}
