package ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states

import android.icu.util.Calendar

data class CalendarDayState(
    val day: Calendar,
    val events: List<EventInCalendarGrid>
)
