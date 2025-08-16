package com.example.financetracker.HomeScreen.TransactionRoom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val amount: Double,
    val type : String,
    val date : Int ,
    val category: String //Income or expense
)