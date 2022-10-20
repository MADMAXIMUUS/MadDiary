package ru.lopata.madDiary.featureReminders.util


import android.icu.util.Calendar
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarItemState

fun getMonth(monthPrevCurCount: Int, monthNextCurCount: Int, referencePoint: Calendar): List<CalendarItemState> {
    val calendarItemStateList = mutableListOf<CalendarItemState>()
    val numberOfMonth = monthNextCurCount + monthPrevCurCount + 1
    referencePoint.add(Calendar.MONTH, -monthPrevCurCount)
    for (i in 0 until numberOfMonth) {
        calendarItemStateList.add(
            CalendarItemState(
                referencePoint.get(Calendar.YEAR),
                referencePoint.get(Calendar.MONTH)
            )
        )
        referencePoint.add(Calendar.MONTH, 1)
    }
    return calendarItemStateList
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
}*//*


private fun List<CalendarDate>.toLog(): String {
    var result = ""
    this.forEach {
        result += it.day.dayOfMonth.toString() + " "
    }
    return result
}*/
