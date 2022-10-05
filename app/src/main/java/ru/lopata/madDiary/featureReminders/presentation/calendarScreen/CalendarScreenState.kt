package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import ru.madmax.madDiary.featureReminders.domain.model.CalendarModel
import java.time.LocalDate

data class CalendarScreenState(
    val selectedDate: LocalDate = LocalDate.now(),
    val calendar: List<CalendarModel> = emptyList()
)
