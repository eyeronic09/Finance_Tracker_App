package com.example.financetracker.HomeScreen.TransactionRoom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converter {

    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_DATE_TIME


    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDate(date: LocalDateTime): String {
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(dateString: String): LocalDateTime{
        return LocalDateTime.parse(dateString , formatter)
    }
}