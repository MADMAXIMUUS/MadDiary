package ru.lopata.madDiary.featureReminders.domain.model

import ru.lopata.madDiary.featureReminders.util.DayType
import java.time.LocalDate

data class CalendarDate(
    val isSelectedDay: Boolean = false,
    val dateType: DayType = DayType.CURRENT_MONTH,
    val day: LocalDate = LocalDate.of(0,0,0),
    val events: List<Event> = emptyList()
)
