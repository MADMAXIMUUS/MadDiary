package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import androidx.annotation.StringRes
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.EditTextState
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
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
    @StringRes
    val repeatTitle: Int = R.string.never,
    val repeat: Long = Repeat.NO_REPEAT,
    val repeatEnd: Date = Date(0),
    @StringRes
    val notificationTitle: List<Int> = listOf(R.string.never),
    val attachment: List<Attachment> = emptyList(),
    val id: Int? = null
)
