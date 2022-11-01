package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarItemState
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarScreenState
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventUseCases: EventUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarScreenState())
    val uiState: StateFlow<CalendarScreenState> = _uiState.asStateFlow()

    init {
        setMonth()
    }

    fun toNextMonth() {
        if (uiState.value.currentPosition < uiState.value.calendar.size - 1) {
            val calendar: Calendar = uiState.value.selectedDay
            calendar.add(Calendar.MONTH, 1)
            _uiState.update { currentState ->
                currentState.copy(
                    selectedDay = calendar,
                    isNeedAnimation = true,
                    currentPosition = currentState.currentPosition + 1
                )
            }
            /*if (uiState.value.currentPosition == uiState.value.calendar.size - 4) {
                val newCalendarList = getMonth(0, 12, uiState.value.selectedDay)
                val tempCalendarList = uiState.value.calendar + newCalendarList
                _uiState.update { currentState ->
                    currentState.copy(
                        calendar = tempCalendarList
                    )
                }
            }*/
        }
    }

    fun toPrevMonth() {
        if (uiState.value.currentPosition > 0) {
            val calendar: Calendar = uiState.value.selectedDay
            calendar.add(Calendar.MONTH, -1)
            _uiState.update { currentState ->
                currentState.copy(
                    selectedDay = calendar,
                    isNeedAnimation = true,
                    currentPosition = currentState.currentPosition - 1
                )
            }
            /*if (listNoteState.value.currentMonthIndex == 3) {
                val newCalendarList = getMonth(12, 0, listNoteState.value.selectedDay)
                val tempCalendarList = newCalendarList + listNoteState.value.calendar
                _listNoteState.update { currentState ->
                    currentState.copy(
                        calendar = tempCalendarList,
                        currentMonthIndex = newCalendarList.size - 1 + currentState.currentMonthIndex
                    )
                }
            }*/
        }
    }

    fun updateSelectedMonthIndex(index: Int) {
        if (index == uiState.value.currentPosition + 1) {
            toNextMonth()
        } else if (index == uiState.value.currentPosition - 1) {
            toPrevMonth()
        }
    }

    fun changeSelectedDate(day: Calendar) {
        if (isPrevMonthSelected(day)) {
            toPrevMonth()
        } else if (isNextMonthSelected(day)) {
            toNextMonth()
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedDay = day
                )
            }
        }
    }

    private fun isPrevMonthSelected(day: Calendar): Boolean {
        return day.get(Calendar.MONTH) < uiState.value.selectedDay.get(Calendar.MONTH) &&
                day.get(Calendar.YEAR) == uiState.value.selectedDay.get(Calendar.YEAR) ||
                day.get(Calendar.MONTH) == Calendar.DECEMBER &&
                day.get(Calendar.YEAR) < uiState.value.selectedDay.get(Calendar.YEAR)
    }

    private fun isNextMonthSelected(day: Calendar): Boolean {
        return day.get(Calendar.MONTH) > uiState.value.selectedDay.get(Calendar.MONTH) &&
                day.get(Calendar.YEAR) == uiState.value.selectedDay.get(Calendar.YEAR) ||
                day.get(Calendar.MONTH) == Calendar.JANUARY &&
                day.get(Calendar.YEAR) > uiState.value.selectedDay.get(Calendar.YEAR)
    }

    private fun setMonth() {
        var monthPrev = 0
        viewModelScope.launch(Dispatchers.IO) {
            eventUseCases.getEventsUseCase().collect { eventAndRepeat ->
                val calendar = Calendar.getInstance()
                if (eventAndRepeat.isNotEmpty()) {
                    val event = eventAndRepeat[0].event
                    calendar.time = event.startDateTime
                    val yearDiff =
                        Calendar.getInstance().get(Calendar.YEAR) - calendar.get(Calendar.YEAR)
                    val monthDiff =
                        Calendar.getInstance().get(Calendar.MONTH) - calendar.get(Calendar.MONTH)
                    monthPrev = yearDiff * 12 + monthDiff
                }
            }
        }
        val calendarList = getMonth(monthPrev, 60, Calendar.getInstance())
        _uiState.update { currentState ->
            currentState.copy(
                calendar = calendarList,
                isNeedAnimation = false,
                currentPosition = monthPrev
            )
        }
    }

    private fun getMonth(
        monthPrevCurCount: Int,
        monthNextCurCount: Int,
        referencePoint: Calendar
    ): List<CalendarItemState> {
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
}