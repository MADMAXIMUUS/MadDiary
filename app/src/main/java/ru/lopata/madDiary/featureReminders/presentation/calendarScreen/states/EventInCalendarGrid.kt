package ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states

import ru.lopata.madDiary.featureReminders.domain.model.Event

data class EventInCalendarGrid(
    val title: String,
    val id: Int,
    val color: Int,
    val pass: Boolean,
    val isChecked: Boolean,
    val type: Event.Types
)