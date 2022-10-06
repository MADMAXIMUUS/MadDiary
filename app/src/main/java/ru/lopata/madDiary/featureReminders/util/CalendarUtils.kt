package ru.lopata.madDiary.featureReminders.util

import android.util.Log
import ru.lopata.madDiary.featureReminders.presentation.calendarView.CalendarDate
import ru.lopata.madDiary.featureReminders.presentation.calendarView.Month
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

fun getMonth(startDate: LocalDate, endDate: LocalDate): List<Month> {
    val monthList = mutableListOf<Month>()
    val numberOfMonth = ChronoUnit.MONTHS.between(startDate, endDate)
    for (i in 0 until numberOfMonth) {
        monthList.add(
            Month(
                YearMonth.of(startDate.year, startDate.month),
                daysInMonthList(startDate)
            )
        )
        startDate.plusMonths(1)
    }
    return monthList
}

private fun daysInMonthList(date: LocalDate): List<CalendarDate> {
    val daysInMonthArray: MutableList<CalendarDate> = mutableListOf()
    val yearMonth: YearMonth = YearMonth.from(date)
    val daysInMonth: Int = yearMonth.lengthOfMonth()
    val firstOfMonth = date.withDayOfMonth(1)
    val dayOfWeek = firstOfMonth.dayOfWeek.value - 1

    val firstOfNextMonth = date.plusMonths(1).withDayOfMonth(1)
    val dayOfNextWeek = firstOfNextMonth.dayOfMonth - 1

    val daysInPrevMonth: Int = yearMonth.minusMonths(1).lengthOfMonth()
    val firstOfPrevMonth = date.minusMonths(1)
        .withDayOfMonth(daysInPrevMonth - dayOfWeek - 1)
    val dayOfPrevWeek = firstOfPrevMonth.dayOfMonth

    for (i in 1..42) {
        when {
            i <= dayOfWeek -> {
                daysInMonthArray.add(
                    CalendarDate(
                        isSelectedDay = false,
                        dateType = DayType.NOT_CURRENT_MONTH,
                        day = LocalDate.of(
                            date.year,
                            date.month - 1,
                            i + dayOfPrevWeek + 1
                        )
                    )
                )
            }
            i > daysInMonth + dayOfWeek -> {
                daysInMonthArray.add(
                    CalendarDate(
                        isSelectedDay = false,
                        dateType = DayType.NOT_CURRENT_MONTH,
                        day = LocalDate.of(
                            date.year,
                            date.month + 1,
                            i - daysInMonth - dayOfWeek + dayOfNextWeek
                        )
                    )
                )
            }
            else -> daysInMonthArray.add(
                if (LocalDate.of(date.year, date.month, i - dayOfWeek) == LocalDate.now()) {
                    CalendarDate(
                        isSelectedDay = false,
                        dateType = DayType.TODAY,
                        day = LocalDate.of(
                            date.year,
                            date.month,
                            i - dayOfWeek
                        )
                    )
                } else {
                    CalendarDate(
                        isSelectedDay = false,
                        day = LocalDate.of(
                            date.year,
                            date.month,
                            i - dayOfWeek
                        )
                    )
                }
            )
        }
    }
    Log.e("calendar", daysInMonthArray.toLog())
    return daysInMonthArray
}

/*private fun List<Month>.toLog(): String {
    var result = ""
    this.forEach {
        it.days.forEach { day ->
            result += day.day.dayOfMonth.toString() + " "
        }
        result += "\n"
    }
    return result
}*/

private fun List<CalendarDate>.toLog(): String {
    var result = ""
    this.forEach {
        result += it.day.dayOfMonth.toString() + " "
    }
    return result
}