package com.example.financetracker.HomeScreen.TransactionRoom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val amount: Double,
    val type : String,
    val date : String ,
    val category: String //Income or expense
)