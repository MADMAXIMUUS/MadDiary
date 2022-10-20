package ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states

import android.icu.util.Calendar

data class CalendarScreenState(
    val selectedDay: Calendar = Calendar.getInstance(),
    val calendar: List<CalendarItemState> = emptyList(),
    val isNeedAnimation: Boolean = false,
    val currentPosition: Int = 0,
)
