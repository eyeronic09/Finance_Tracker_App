package com.example.financetracker.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val goalId: Int = 0,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val deadline: LocalDateTime? = null
)
