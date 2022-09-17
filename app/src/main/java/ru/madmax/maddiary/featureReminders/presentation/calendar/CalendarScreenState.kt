package ru.madmax.madDiary.featureReminders.presentation.calendar

import ru.madmax.madDiary.featureReminders.domain.model.CalendarModel
import java.time.LocalDate

data class CalendarScreenState(
    val selectedDate: LocalDate = LocalDate.now(),
    val calendar: List<CalendarModel> = emptyList()
)
