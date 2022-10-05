package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.madmax.madDiary.featureReminders.domain.model.CalendarModel
import ru.madmax.madDiary.featureReminders.domain.useCase.event.EventUseCases
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventUseCases: EventUseCases
) : ViewModel() {

    private val _listNoteState = MutableStateFlow(CalendarScreenState())
    val listNoteState: StateFlow<CalendarScreenState> = _listNoteState

    init {
        setMonth()
    }

    private fun daysInMonthArray(date: LocalDate): List<LocalDate> {
        val daysInMonthArray: MutableList<LocalDate> = mutableListOf()
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
                        LocalDate.of(
                            _listNoteState.value.selectedDate.year,
                            _listNoteState.value.selectedDate.month - 1,
                            i + dayOfPrevWeek + 1
                        )
                    )
                }
                i > daysInMonth + dayOfWeek -> {
                    daysInMonthArray.add(
                        LocalDate.of(
                            _listNoteState.value.selectedDate.year,
                            _listNoteState.value.selectedDate.month + 1,
                            i - daysInMonth - dayOfWeek + dayOfNextWeek
                        )
                    )
                }
                else -> daysInMonthArray.add(
                    LocalDate.of(
                        _listNoteState.value.selectedDate.year,
                        _listNoteState.value.selectedDate.month,
                        i - dayOfWeek
                    )
                )
            }
        }
        return daysInMonthArray
    }

    fun changeSelectedDate(day: LocalDate) {
        _listNoteState.value = listNoteState.value.copy(
            selectedDate = day
        )
        setMonth()
    }

    fun toPrevMonth() {
        _listNoteState.value = listNoteState.value.copy(
            selectedDate = listNoteState.value.selectedDate.minusMonths(1)
        )
        setMonth()
    }

    fun toNextMonth() {
        _listNoteState.value = listNoteState.value.copy(
            selectedDate = listNoteState.value.selectedDate.plusMonths(1)
        )
        setMonth()
    }

    private fun setMonth() {
        val days = daysInMonthArray(listNoteState.value.selectedDate)
        val calendarList: MutableList<CalendarModel> = mutableListOf()
        days.forEach { date ->
            if (date.month == listNoteState.value.selectedDate.month) {
                if (date == listNoteState.value.selectedDate) {
                    calendarList.add(
                        CalendarModel(
                            isSelectedDay = true,
                            isCurrentMonth = true,
                            day = date,
                            events = emptyList()
                        )
                    )
                } else {
                    calendarList.add(
                        CalendarModel(
                            isSelectedDay = false,
                            isCurrentMonth = true,
                            day = date,
                            events = emptyList()
                        )
                    )
                }
            } else {
                calendarList.add(
                    CalendarModel(
                        isSelectedDay = false,
                        isCurrentMonth = false,
                        day = date,
                        events = emptyList()
                    )
                )
            }
        }
        _listNoteState.value = listNoteState.value.copy(
            calendar = calendarList
        )
    }
}