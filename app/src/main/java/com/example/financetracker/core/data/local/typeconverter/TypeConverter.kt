package com.example.financetracker.core.data.local.typeconverter

import androidx.room.Entity
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TypeConverter {

    @TypeConverter
    fun LDTtoString(value: LocalDateTime?): String {
        return value.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun localDateToString(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

}