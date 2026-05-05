package com.example.financetracker.core.data.repository

import com.example.financetracker.core.data.local.dao.GoalDao
import com.example.financetracker.core.data.local.mapper.toDomain
import com.example.financetracker.core.data.local.mapper.toEntity
import com.example.financetracker.core.domain.model.Goal
import com.example.financetracker.core.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoalRepositoryImpl(
    private val goalDao: GoalDao
) : GoalRepository {
    override fun getAllGoals(): Flow<List<Goal>> {
        return goalDao.getAllGoals().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertGoal(goal: Goal) {
        goalDao.insertGoal(goal.toEntity())
    }

    override suspend fun updateGoal(goal: Goal) {
        goalDao.updateGoal(goal.toEntity())
    }

    override suspend fun deleteGoal(goal: Goal) {
        goalDao.deleteGoal(goal.toEntity())
    }

    override suspend fun addFundsToGoal(goalId: Int, amount: Double) {
        goalDao.addFundsToGoal(goalId, amount)
    }
}
