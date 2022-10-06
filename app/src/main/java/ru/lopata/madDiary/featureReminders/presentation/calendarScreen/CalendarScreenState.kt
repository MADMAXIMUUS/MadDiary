package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import ru.lopata.madDiary.featureReminders.presentation.calendarView.Month
import java.time.LocalDate

data class CalendarScreenState(
    val today: LocalDate = LocalDate.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val calendar: List<Month> = emptyList(),
    val currentMonthIndex: Int = calendar.size / 2,
)
