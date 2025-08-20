package com.example.financetracker.HomeScreen.TransactionRoom

import androidx.compose.runtime.remember
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val balance: Double,
    val amount: Double,
    val type: String,
    val date: Long,
)