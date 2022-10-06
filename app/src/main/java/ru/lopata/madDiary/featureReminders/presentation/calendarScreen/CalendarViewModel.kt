package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import ru.lopata.madDiary.featureReminders.util.getMonth
import java.time.LocalDate
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

    fun toNextMonth() {
        _listNoteState.value = listNoteState.value.copy(
            currentMonthIndex = listNoteState.value.currentMonthIndex + 1
        )
    }

    fun toPrevMonth() {
        _listNoteState.value = listNoteState.value.copy(
            currentMonthIndex = listNoteState.value.currentMonthIndex - 1
        )
    }

    fun updateSelectedMonthIndex(index: Int) {
        _listNoteState.value = listNoteState.value.copy(
            currentMonthIndex = index
        )
    }

    fun changeSelectedDate(day: LocalDate) {
        _listNoteState.value = listNoteState.value.copy(
            selectedDate = day
        )
    }

    private fun setMonth() {
        val calendarList = getMonth(
            listNoteState.value.selectedDate.minusYears(1),
            listNoteState.value.selectedDate.plusYears(1)
        )
        _listNoteState.value = listNoteState.value.copy(
            calendar = calendarList,
            currentMonthIndex = calendarList.size / 2
        )
    }
}