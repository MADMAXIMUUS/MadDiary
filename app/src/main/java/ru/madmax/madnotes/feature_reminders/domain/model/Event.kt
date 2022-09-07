package ru.madmax.madnotes.feature_reminders.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.madmax.madnotes.core.util.Converters
import java.util.*

@Entity(tableName = "EVENTS")
data class Event(
    val title: String = "",
    val startTimestamp: Date = Date(0),
    val endTimestamp: Date = Date(0),
    val location: String = "",
    val note: String = "",
    val repeat: String = "Every week",
    val notification: String = "none",
    val attachment: String = "empty",

    @PrimaryKey(autoGenerate = true) val eventId: Int? = null
)