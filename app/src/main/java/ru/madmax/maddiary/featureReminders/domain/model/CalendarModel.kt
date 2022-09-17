package ru.madmax.madDiary.featureReminders.domain.model

import java.time.LocalDate

data class CalendarModel(
    val isSelectedDay: Boolean = false,
    val isCurrentMonth: Boolean = true,
    val day: LocalDate = LocalDate.of(0, 0, 0),
    val events: List<Event> = emptyList()
)
