package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.EventUseCases
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarDayState
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarScreenState
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.CalendarViewState
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendarGrid
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventsUseCases: EventUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarScreenState())
    val uiState: StateFlow<CalendarScreenState> = _uiState.asStateFlow()

    private var job: Job? = null

    init {
        getEvents()
    }

    private fun getEvents() {
        job?.cancel()
        job = eventsUseCases.getEventsUseCase()
            .onEach { events ->
                val map = mutableMapOf<Calendar, MutableList<EventInCalendarGrid>>()
                val today = Calendar.getInstance()
                events.forEach { eventAndRepeat ->
                    val event = eventAndRepeat.event
                    val repeat = eventAndRepeat.repeat ?: Repeat()
                    var date = event.startDateTime.time
                    val diffDate = abs(event.endDateTime.time - event.startDateTime.time)
                    val diffDay = TimeUnit.DAYS.convert(diffDate, TimeUnit.MILLISECONDS)
                    var chapter = 1
                    while (date <= event.endDateTime.time) {
                        var calendar = Calendar.getInstance()
                        calendar.timeInMillis = date

                        calendar.apply {
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.HOUR, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                            set(Calendar.MILLISECONDS_IN_DAY, 0)
                        }

                        if (map[calendar] == null)
                            map[calendar] = mutableListOf()

                        var pass = date < today.timeInMillis

                        val item = EventInCalendarGrid(
                            title = event.title,
                            id = event.eventId!!,
                            color = event.color,
                            isChecked = event.completed,
                            pass = pass,
                            type = event.type
                        )

                        map[calendar]?.add(item)

                        if (repeat.repeatInterval != Repeat.NO_REPEAT) {
                            var interval = repeat.repeatInterval
                            var i = 1
                            while (event.startDateTime.time + diffDay + interval < repeat.repeatEnd.time + DAY_IN_MILLISECONDS) {
                                when (repeat.repeatInterval) {
                                    Repeat.EVERY_DAY, Repeat.EVERY_SECOND_DAY -> {
                                        calendar = Calendar.getInstance()

                                        calendar.timeInMillis =
                                            date + i * (diffDay * DAY_IN_MILLISECONDS) + interval

                                        calendar.apply {
                                            set(Calendar.HOUR_OF_DAY, 0)
                                            set(Calendar.HOUR, 0)
                                            set(Calendar.MINUTE, 0)
                                            set(Calendar.SECOND, 0)
                                            set(Calendar.MILLISECOND, 0)
                                            set(Calendar.MILLISECONDS_IN_DAY, 0)
                                        }
                                    }
                                    Repeat.EVERY_WEEK -> {
                                        calendar.add(Calendar.WEEK_OF_YEAR, 1)
                                    }
                                    Repeat.EVERY_SECOND_WEEK -> {
                                        calendar.add(Calendar.WEEK_OF_YEAR, 2)
                                    }
                                    Repeat.EVERY_MONTH -> {
                                        calendar.add(Calendar.MONTH, 1)
                                    }
                                    Repeat.EVERY_YEAR -> {
                                        calendar.add(Calendar.YEAR, 1)
                                    }
                                }

                                if (map[calendar] == null)
                                    map[calendar] = mutableListOf()

                                pass = calendar.timeInMillis < today.timeInMillis

                                map[calendar]?.add(item.copy(pass = pass))

                                interval += repeat.repeatInterval
                                i++
                            }
                        }
                        chapter++
                        date += DAY_IN_MILLISECONDS

                    }
                }
                val calendarViewStates = mutableListOf<CalendarViewState>()
                val calendarDayStates = mutableListOf<CalendarDayState>()
                val sortedMap = TreeMap(map)
                if (sortedMap.keys.isNotEmpty()) {
                    var currentDate = sortedMap.firstKey()
                    sortedMap.keys.forEach { date ->
                        if (date.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH) ||
                            date.get(Calendar.MONTH) != currentDate.get(Calendar.MONTH)
                        ) {
                            calendarViewStates.add(
                                CalendarViewState(
                                    yearNumber = currentDate.get(Calendar.YEAR),
                                    monthNumber = currentDate.get(Calendar.MONTH),
                                    events = calendarDayStates.toList()
                                )
                            )
                            calendarDayStates.clear()
                            currentDate = date
                        }
                        calendarDayStates.add(
                            CalendarDayState(
                                date,
                                events = sortedMap[date]!!
                            )
                        )
                        currentDate = date
                    }
                    if (calendarDayStates.isNotEmpty())
                        calendarViewStates.add(
                            CalendarViewState(
                                yearNumber = currentDate.get(Calendar.YEAR),
                                monthNumber = currentDate.get(Calendar.MONTH),
                                events = calendarDayStates.toList()
                            )
                        )
                }
                val calendarEvents = mutableListOf<CalendarViewState>()
                calendarEvents.addAll(calendarViewStates)
                var monthPrev = 0
                var monthNext = 60
                val calendar = Calendar.getInstance()
                if (calendarEvents.isNotEmpty()) {
                    calendar.apply {
                        set(Calendar.MONTH, calendarEvents[0].monthNumber)
                        set(Calendar.YEAR, calendarEvents[0].yearNumber)
                    }
                    var yearDiff =
                        Calendar.getInstance().get(Calendar.YEAR) - calendar.get(Calendar.YEAR)
                    var monthDiff =
                        Calendar.getInstance().get(Calendar.MONTH) - calendar.get(Calendar.MONTH)
                    monthPrev = yearDiff * 12 + monthDiff
                    calendar.apply {
                        set(Calendar.MONTH, calendarEvents[calendarEvents.size - 1].monthNumber)
                        set(Calendar.YEAR, calendarEvents[calendarEvents.size - 1].yearNumber)
                    }
                    yearDiff = calendar.get(Calendar.YEAR) -
                            Calendar.getInstance().get(Calendar.YEAR)
                    monthDiff = calendar.get(Calendar.MONTH) -
                            Calendar.getInstance().get(Calendar.MONTH)
                    val diff = yearDiff * 12 + monthDiff
                    if (diff > 60)
                        monthNext = diff
                }
                val monthList = getMonths(monthPrev, monthNext, Calendar.getInstance())
                val calendarList = mutableListOf<CalendarViewState>()
                var j = 0
                if (calendarEvents.isNotEmpty()) {
                    for (i in monthList.indices) {
                        if (j < calendarEvents.size &&
                            monthList[i].monthNumber == calendarEvents[j].monthNumber &&
                            monthList[i].yearNumber == calendarEvents[j].yearNumber
                        ) {
                            calendarList.add(calendarEvents[j])
                            j++
                        } else {
                            calendarList.add(monthList[i])
                        }
                    }
                } else {
                    calendarList.addAll(monthList)
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        calendar = calendarList,
                        isNeedAnimation = false,
                        currentPosition = monthPrev
                    )
                }
            }
            .launchIn(viewModelScope)
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

    private fun getMonths(
        monthPrevCurCount: Int,
        monthNextCurCount: Int,
        referencePoint: Calendar
    ): List<CalendarViewState> {
        val calendarViewStateList = mutableListOf<CalendarViewState>()
        val numberOfMonth = monthNextCurCount + monthPrevCurCount + 1
        referencePoint.add(Calendar.MONTH, -monthPrevCurCount)
        for (i in 0 until numberOfMonth) {
            calendarViewStateList.add(
                CalendarViewState(
                    referencePoint.get(Calendar.YEAR),
                    referencePoint.get(Calendar.MONTH)
                )
            )
            referencePoint.add(Calendar.MONTH, 1)
        }
        return calendarViewStateList
    }

    fun updateCheckState(eventId: Int, state: Boolean) {
        viewModelScope.launch {
            eventsUseCases.getEventByIdUseCase(eventId)?.let {
                val event = it.event.copy(completed = state)
                eventsUseCases.createEventUseCase(event)
            }
        }
    }

    private companion object {
        const val DAY_IN_MILLISECONDS = 86400000
    }
}