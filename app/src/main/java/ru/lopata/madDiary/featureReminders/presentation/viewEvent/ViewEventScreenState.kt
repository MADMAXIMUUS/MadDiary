package ru.lopata.madDiary.featureReminders.presentation.viewEvent

import androidx.annotation.StringRes
import ru.lopata.madDiary.R
import java.sql.Date

data class ViewEventScreenState(
    val title: String = "",
    val completed: Boolean = false,
    val cover: String = "empty",
    val startDateTime: Date = Date(0),
    val endDateTime: Date = Date(0),
    val chapter: Int = 1,
    val chapters: Int = 1,
    val allDay: Boolean = false,
    val color: Int = -1,
    val location: String = "",
    val note: String = "",
    val notification: String = "Never",
    @StringRes
    val repeat: Int = R.string.never,
    val repeatEnd: Date = Date(0),
    val attachment: String = "empty",
    val eventId: Int = -1
)