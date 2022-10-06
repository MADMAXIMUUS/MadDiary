package ru.lopata.madDiary.core.util

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = value
        return calendar
    }

    @TypeConverter
    fun dateToTimestamp(date: Calendar): Long {
        return date.timeInMillis
    }
}