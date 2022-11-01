package ru.lopata.madDiary.featureReminders.presentation.listReminders

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.toDate
import ru.lopata.madDiary.core.util.toTime
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import java.sql.Date
import java.util.TreeMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ListEventViewModel @Inject constructor(
    private val eventsUseCases: EventUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListEventScreenState())
    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    init {
        getEvents()
    }

    private fun getEvents() {
        job?.cancel()
        job = eventsUseCases.getEventsUseCase()
            .onEach { events ->
                val map = mutableMapOf<Date, MutableList<MainScreenItem>>()
                events.forEach { eventAndRepeat ->
                    val event = eventAndRepeat.event
                    val repeat = eventAndRepeat.repeat ?: Repeat()
                    var date = event.startDateTime.time
                    while (date <= event.endDateTime.time) {
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = date

                        val dateWithoutTime = Calendar.getInstance().apply {
                            set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))
                            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.HOUR, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        if (map[Date(dateWithoutTime.timeInMillis)] == null)
                            map[Date(dateWithoutTime.timeInMillis)] = mutableListOf()

                        val startTime = if (date == event.startDateTime.time)
                            event.startDateTime.time.toTime()
                        else ""

                        val endDate = event.endDateTime.time
                        val endTime = if (date < endDate && date + DAY_IN_MILLISECONDS > endDate)
                            event.endDateTime.time.toTime()
                        else ""

                        map[Date(dateWithoutTime.timeInMillis)]?.add(
                            MainScreenItem.EventItem(
                                id = event.eventId!!,
                                title = event.title,
                                isChecked = event.completed,
                                type = MainScreenItem.EVENT,
                                startTime = startTime,
                                endTime = endTime,
                                address = /*event.location*/ "Улица Ленина, 2",
                                color = event.color,
                                cover = "",
                                isNotificationSet = false
                            )
                        )


                        if (repeat.repeatInterval != Repeat.NO_REPEAT) {
                            var interval = repeat.repeatInterval
                            var i = 1
                            while (interval <= repeat.repeatInterval * 6) {
                                when (repeat.repeatInterval) {
                                    Repeat.EVERY_DAY, Repeat.EVERY_SECOND_DAY -> {
                                        val diffDate = abs(endDate - event.startDateTime.time)
                                        val diffDay =
                                            TimeUnit.DAYS.convert(diffDate, TimeUnit.MILLISECONDS)
                                        calendar.timeInMillis =
                                            date + i * (diffDay * DAY_IN_MILLISECONDS) + interval

                                        dateWithoutTime.apply {
                                            set(
                                                Calendar.DAY_OF_MONTH,
                                                calendar.get(Calendar.DAY_OF_MONTH)
                                            )
                                            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                                            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                                        }
                                    }
                                    Repeat.EVERY_WEEK -> {
                                        dateWithoutTime.add(Calendar.WEEK_OF_YEAR, 1)
                                    }
                                    Repeat.EVERY_SECOND_WEEK -> {
                                        dateWithoutTime.add(Calendar.WEEK_OF_YEAR, 2)
                                    }
                                    Repeat.EVERY_MONTH -> {
                                        dateWithoutTime.add(Calendar.MONTH, 1)
                                    }
                                    Repeat.EVERY_YEAR -> {
                                        dateWithoutTime.add(Calendar.YEAR, 1)
                                    }
                                }

                                if (map[Date(dateWithoutTime.timeInMillis)] == null)
                                    map[Date(dateWithoutTime.timeInMillis)] = mutableListOf()

                                map[Date(dateWithoutTime.timeInMillis)]?.add(
                                    MainScreenItem.EventItem(
                                        id = event.eventId!!,
                                        title = event.title,
                                        isChecked = event.completed,
                                        type = MainScreenItem.EVENT,
                                        startTime = startTime,
                                        endTime = endTime,
                                        address = event.location,
                                        color = event.color,
                                        cover = "",
                                        isNotificationSet = false
                                    )
                                )
                                interval += repeat.repeatInterval
                                i++
                            }
                        }

                        date += DAY_IN_MILLISECONDS

                    }
                }
                val list = mutableListOf<MainScreenItem>()
                val sortedMap = TreeMap(map)
                sortedMap.keys.forEach { date ->

                    val today = Calendar.getInstance().timeInMillis
                    val tomorrow = today + DAY_IN_MILLISECONDS
                    val yesterday = today - DAY_IN_MILLISECONDS

                    val title = if (date.time <= today && date.time + DAY_IN_MILLISECONDS >= today)
                        R.string.today
                    else if (date.time <= tomorrow && date.time + DAY_IN_MILLISECONDS >= tomorrow)
                        R.string.tomorrow
                    else if (date.time <= yesterday && date.time + DAY_IN_MILLISECONDS >= yesterday)
                        R.string.yesterday
                    else -1

                    list.add(
                        MainScreenItem.TitleItem(
                            title = title,
                            date = date.time.toDate()
                        )
                    )

                    sortedMap[date]?.forEach {
                        list.add(it)
                    }

                }
                _uiState.update { currentState ->
                    currentState.copy(
                        events = list.toList()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val DAY_IN_MILLISECONDS = 86400000
    }

}