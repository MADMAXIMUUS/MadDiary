package ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states

data class CalendarViewState(
    val yearNumber: Int = 1970,
    val monthNumber: Int = 1,
    val initialSelectedDay: Int = -1,
    val events: List<CalendarDayState> = emptyList()
)
