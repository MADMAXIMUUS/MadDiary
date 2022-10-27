package ru.lopata.madDiary.core.util

import androidx.room.TypeConverter
import java.sql.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        val date = Date(value)
        return date
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}