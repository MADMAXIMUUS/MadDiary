package ru.lopata.madDiary.featureReminders.presentation.calendarView

import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.util.DayType
import java.time.LocalDate

data class CalendarDate(
    val day: LocalDate = LocalDate.of(0,0,0),
    val events: List<Event> = emptyList()
)
