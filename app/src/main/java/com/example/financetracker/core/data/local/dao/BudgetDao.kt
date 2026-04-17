package com.example.financetracker.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetracker.core.data.local.entity.BudgetEntity
import java.time.LocalDateTime

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget ")
    suspend fun getBudget(): BudgetEntity?

    @Query("SELECT amount FROM budget WHERE strftime('%Y-%m', startDate) = strftime('%Y-%m', :currentDate)")
    suspend fun getBudgetAmountForCurrentMonth(currentDate: LocalDateTime): Double?

    @Insert
    suspend fun insert(budget: BudgetEntity)

    @Update
    suspend fun update(budget: BudgetEntity)

    @Query("UPDATE budget SET amount = :amount")
    suspend fun updateBudgetAmount(amount: Double)

    @Query("DELETE FROM budget")
    suspend fun clear()
}
