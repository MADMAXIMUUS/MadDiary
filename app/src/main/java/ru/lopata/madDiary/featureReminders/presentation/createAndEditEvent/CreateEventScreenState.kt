package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import ru.lopata.madDiary.core.util.EditTextState
import java.sql.Date

data class CreateEventScreenState(
    val title: EditTextState = EditTextState(),
    val completed: Boolean = false,
    val startDateTime: Date = Date(0),
    val isStartDateError: Boolean = false,
    val endDateTime: Date = Date(0),
    val isEndDateError: Boolean = false,
    val allDay: Boolean = false,
    val color: Int = -1,
    val location: String = "",
    val note: String = "",
    val repeat: String = "Never",
    val notification: String = "Never",
    val attachment: String = "empty",
    val id: Int? = null
)
