package ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states

import android.icu.util.Calendar
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendar

data class CalendarEvent(
    val day: Calendar,
    val events: List<EventInCalendar>
)
