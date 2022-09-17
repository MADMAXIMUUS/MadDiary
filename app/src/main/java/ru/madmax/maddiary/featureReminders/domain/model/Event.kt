package ru.madmax.madDiary.featureReminders.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "EVENTS")
data class Event(
    val title: String = "",
    val completed: Boolean = false,
    val startTimestamp: Date = Date(0),
    val endTimestamp: Date = Date(0),
    val allDay: Boolean = false,
    val location: String = "",
    val note: String = "",
    val repeat: String = "Never",
    val notification: String = "Never",
    val attachment: String = "empty",

    @PrimaryKey(autoGenerate = true) val eventId: Int? = null
)