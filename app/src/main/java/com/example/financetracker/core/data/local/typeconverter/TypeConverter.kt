package com.example.financetracker.core.data.local.typeconverter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

class TypeConverter {

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return if (value == null || value == "null") null else LocalDateTime.parse(value)
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return if (value == null || value == "null") null else LocalDate.parse(value)
    }
}
