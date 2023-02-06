package ru.lopata.madDiary.featureReminders.presentation.listEvents

import android.icu.util.Calendar
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.toDate
import ru.lopata.madDiary.core.util.toTimeZone
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.domain.model.Notification
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import java.sql.Date
import java.util.*
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

    fun updateEventState(id: Int, state: Boolean) {
        viewModelScope.launch {
            eventsUseCases.getEventByIdUseCase(id)?.let {
                val event = it.event.copy(completed = state)
                eventsUseCases.createEventUseCase(event)
            }
        }
    }

    private fun getEvents() {
        job?.cancel()
        job = eventsUseCases.getEventsUseCase()
            .onEach { events ->
                val map = mutableMapOf<Date, MutableList<MainScreenItem>>()
                events.forEach { eventRepeatNotificationsAttachments ->
                    val event = eventRepeatNotificationsAttachments.event
                    val repeat = eventRepeatNotificationsAttachments.repeat ?: Repeat()
                    val attachments = eventRepeatNotificationsAttachments.attachments
                    val notifications = eventRepeatNotificationsAttachments.notifications
                    var date = event.startDateTime.time
                    val diffDate = abs(event.endDateTime.time - event.startDateTime.time)
                    val diffDay = TimeUnit.DAYS.convert(diffDate, TimeUnit.MILLISECONDS)
                    var chapter = 1
                    while (date <= event.endDateTime.time) {
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = date

                        calendar.apply {
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.HOUR, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                            set(Calendar.MILLISECONDS_IN_DAY, 0)
                        }

                        if (map[Date(calendar.timeInMillis)] == null)
                            map[Date(calendar.timeInMillis)] = mutableListOf()

                        val startTime = if (!event.allDay && date == event.startDateTime.time)
                            event.startDateTime.time.toTimeZone()
                        else ""

                        val endDate = event.endDateTime.time
                        val endTime =
                            if (!event.allDay && (date < endDate && date + DAY_IN_MILLISECONDS > endDate || date == endDate))
                                event.endDateTime.time.toTimeZone()
                            else ""

                        val isNotificationSet = notifications.isNotEmpty() &&
                                notifications[0].time != Notification.NEVER

                        map[Date(calendar.timeInMillis)]?.add(
                            MainScreenItem.EventItem(
                                id = event.eventId!!,
                                chapter = chapter,
                                chapters = diffDay.toInt() + 1,
                                title = event.title,
                                isChecked = event.completed,
                                type = MainScreenItem.EVENT,
                                startTime = startTime,
                                endTime = endTime,
                                address = event.location,
                                color = event.color,
                                isAttachmentAdded = attachments.isNotEmpty(),
                                cover = Uri.parse(event.cover),
                                isNotificationSet = isNotificationSet
                            )
                        )


                        if (repeat.repeatInterval != Repeat.NO_REPEAT) {
                            var interval = repeat.repeatInterval
                            var i = 1
                            while (event.startDateTime.time + diffDay + interval <= repeat.repeatEnd.time + DAY_IN_MILLISECONDS) {
                                when (repeat.repeatInterval) {
                                    Repeat.EVERY_DAY, Repeat.EVERY_SECOND_DAY -> {

                                        calendar.timeInMillis =
                                            date + i * (diffDay * DAY_IN_MILLISECONDS) + interval

                                        calendar.apply {
                                            set(
                                                Calendar.DAY_OF_MONTH,
                                                calendar.get(Calendar.DAY_OF_MONTH)
                                            )
                                            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                                            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
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

                                if (map[Date(calendar.timeInMillis)] == null)
                                    map[Date(calendar.timeInMillis)] = mutableListOf()

                                map[Date(calendar.timeInMillis)]?.add(
                                    MainScreenItem.EventItem(
                                        id = event.eventId!!,
                                        title = event.title,
                                        chapter = chapter,
                                        chapters = diffDay.toInt() + 1,
                                        isChecked = event.completed,
                                        type = MainScreenItem.EVENT,
                                        startTime = startTime,
                                        endTime = endTime,
                                        address = event.location,
                                        color = event.color,
                                        isAttachmentAdded = attachments.isNotEmpty(),
                                        cover = Uri.parse(event.cover),
                                        isNotificationSet = isNotificationSet
                                    )
                                )
                                interval += repeat.repeatInterval
                                i++
                            }
                        }
                        chapter++
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