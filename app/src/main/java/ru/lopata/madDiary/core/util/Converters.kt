package ru.lopata.madDiary.core.util

import androidx.room.TypeConverter
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.Event.Types.Companion.toTypesEnum
import java.sql.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun enumToString(enum: Event.Types): String {
        return enum.name
    }

    @TypeConverter
    fun stringToEnum(name: String): Event.Types {
        return name.toTypesEnum()
    }

}