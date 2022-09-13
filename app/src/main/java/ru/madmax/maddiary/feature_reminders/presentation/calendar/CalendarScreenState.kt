package ru.madmax.maddiary.feature_reminders.presentation.calendar

import ru.madmax.maddiary.feature_reminders.domain.model.CalendarModel
import java.time.LocalDate

data class CalendarScreenState(
    val selectedDate: LocalDate = LocalDate.now(),
    val calendar: List<CalendarModel> = emptyList()
)
