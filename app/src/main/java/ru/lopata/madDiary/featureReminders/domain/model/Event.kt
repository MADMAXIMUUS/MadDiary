package ru.lopata.madDiary.featureReminders.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity(tableName = "EVENTS")
data class Event(
    val title: String = "",
    val completed: Boolean = false,
    val startDateTime: Calendar = Calendar.getInstance(),
    val endDateTime: Calendar = Calendar.getInstance(),
    val allDay: Boolean = false,
    val color: Int = -1,
    val location: String = "",
    val note: String = "",
    val repeat: String = "Never",
    val notification: String = "Never",
    val attachment: String = "empty",

    @PrimaryKey(autoGenerate = true) val eventId: Int? = null
)