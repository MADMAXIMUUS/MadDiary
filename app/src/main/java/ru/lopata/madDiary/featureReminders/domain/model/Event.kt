package ru.lopata.madDiary.featureReminders.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendar
import java.sql.Date

@Entity(tableName = "EVENTS")
data class Event(
    val title: String = "",
    val completed: Boolean = false,
    val startDateTime: Date = Date(0),
    val endDateTime: Date = Date(0),
    val allDay: Boolean = false,
    val color: Int = -1,
    val location: String = "",
    val note: String = "",
    val repeat: String = "Never",
    val notification: String = "Never",
    val attachment: String = "empty",

    @PrimaryKey(autoGenerate = true) val eventId: Int? = null
) {
    fun toEventInCalendar(): EventInCalendar {
        return EventInCalendar(
            title = title,
            color = color
        )
    }
}