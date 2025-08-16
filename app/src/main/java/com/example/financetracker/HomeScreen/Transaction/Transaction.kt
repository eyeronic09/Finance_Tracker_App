package com.example.financetracker.HomeScreen.Transaction

import androidx.room.PrimaryKey

data class Transaction(
    @PrimaryKey(autoGenerate = true) val id : Int,
    val amount: Double,
    val type : String,
    val date : Int ,
    val category: String //Income or expense
)