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


    @Query("SELECT amount FROM budget WHERE :date >= startDate AND :date <= endDate")
    suspend fun getBudgetAmountForCurrentMonth(date: LocalDateTime): Double?

    @Query("UPDATE budget SET amount = amount + :previousMouthsAmounts WHERE :date >= startDate AND :date <= endDate")
    suspend fun getCurrentMonthsBudgetForRollOver(date: LocalDateTime, previousMouthsAmounts: Double): Int

    @Query("SELECT amount FROM budget WHERE :previousMonthDate >= startDate AND :previousMonthDate <= endDate")
    suspend fun getPreviousMonthBudget(previousMonthDate: LocalDateTime): Double?

    @Insert
    suspend fun insert(budget: BudgetEntity)

    @Update
    suspend fun update(budget: BudgetEntity)

    @Query("UPDATE budget SET amount = amount + :amount WHERE :date >= startDate AND :date <= endDate")
    suspend fun updateBudgetAmount(amount: Double, date: LocalDateTime)

    @Query("UPDATE budget SET amount = amount - :amount WHERE :date >= startDate AND :date <= endDate")
    suspend fun updateBudgetAmountMinus(amount: Double, date: LocalDateTime)


    @Query("DELETE FROM budget")
    suspend fun clear()
}
