package ru.lopata.madDiary.featureReminders.presentation.listEvents

import android.content.ContentResolver
import android.icu.util.Calendar
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.BuildConfig
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.toDate
import ru.lopata.madDiary.core.util.toMonth
import ru.lopata.madDiary.core.util.toTimeZone
import ru.lopata.madDiary.featureReminders.domain.model.*
import ru.lopata.madDiary.featureReminders.domain.useCase.EventUseCases
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DateItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.EventItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.MonthYearTitleItem
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
                val map = mutableMapOf<Date, MutableList<DelegateAdapterItem>>()
                val today = Calendar.getInstance()
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

                        var subtitleFrom: Int = -1
                        var subtitleTo: Int = -1

                        when (event.type) {
                            Event.Types.EVENT -> {
                                subtitleFrom = R.string.from
                                subtitleTo = R.string.to
                            }
                            Event.Types.TASK -> {
                                subtitleFrom = R.string.reminder_task_deadline
                                subtitleTo = -1
                            }
                            Event.Types.REMINDER -> {
                                subtitleFrom = R.string.reminder_reminder_date
                                subtitleTo = -1
                            }
                        }

                        var pass = date < today.timeInMillis

                        val item = EventItem(
                            id = event.eventId!!,
                            chapter = chapter,
                            chapters = diffDay.toInt() + 1,
                            title = event.title,
                            isChecked = event.completed,
                            type = event.type,
                            pass = pass,
                            startTime = startTime,
                            subtitleFrom = subtitleFrom,
                            subtitleTo = subtitleTo,
                            endTime = endTime,
                            address = event.location,
                            color = event.color,
                            isAttachmentAdded = attachments.isNotEmpty(),
                            cover = Uri.parse(event.cover),
                            isNotificationSet = isNotificationSet
                        )

                        map[Date(calendar.timeInMillis)]?.add(item)


                        if (repeat.repeatInterval != Repeat.NO_REPEAT) {
                            var interval = repeat.repeatInterval
                            var i = 1
                            while (event.startDateTime.time + diffDay + interval < repeat.repeatEnd.time + DAY_IN_MILLISECONDS) {
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

                                pass = calendar.timeInMillis < today.timeInMillis

                                map[Date(calendar.timeInMillis)]?.add(item.copy(pass = pass))

                                interval += repeat.repeatInterval
                                i++
                            }
                        }
                        chapter++
                        date += DAY_IN_MILLISECONDS

                    }
                }
                val list = mutableListOf<DelegateAdapterItem>()
                val sortedMap = TreeMap(map)
                var i = 0
                var todayId = 0
                val calendar = Calendar.getInstance()
                var prevMonth = ""
                if (sortedMap.keys.isNotEmpty()) {
                    calendar.timeInMillis = sortedMap.keys.first().time
                    prevMonth = calendar.timeInMillis.toMonth()
                    list.add(
                        MonthYearTitleItem(
                            month = prevMonth,
                            yearNumber = calendar.get(Calendar.YEAR),
                            getURLForResource(calendar.get(Calendar.MONTH))
                        )
                    )
                }
                sortedMap.keys.forEach { date ->
                    val tomorrow = today.timeInMillis + DAY_IN_MILLISECONDS
                    val yesterday = today.timeInMillis - DAY_IN_MILLISECONDS

                    val title =
                        if (date.time <= today.timeInMillis && date.time + DAY_IN_MILLISECONDS >= today.timeInMillis)
                            R.string.today
                        else if (date.time <= tomorrow && date.time + DAY_IN_MILLISECONDS >= tomorrow)
                            R.string.tomorrow
                        else if (date.time <= yesterday && date.time + DAY_IN_MILLISECONDS >= yesterday)
                            R.string.yesterday
                        else -1

                    if (title == R.string.today) {
                        todayId = i
                    }
                    calendar.timeInMillis = date.time
                    if (date.time.toMonth() != prevMonth) {
                        prevMonth = date.time.toMonth()
                        list.add(
                            MonthYearTitleItem(
                                month = prevMonth,
                                yearNumber = calendar.get(Calendar.YEAR),
                                getURLForResource(calendar.get(Calendar.MONTH))
                            )
                        )
                    }

                    list.add(
                        DateItem(
                            title = title,
                            date = date.time.toDate()
                        )
                    )

                    sortedMap[date]?.forEach {
                        list.add(it)
                    }
                    i++
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        events = list.toList(),
                        todayId = todayId
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun undoDelete(item: EventRepeatNotificationAttachment) {
        viewModelScope.launch(IO) {
            val event = item.event.copy(
                eventId = null
            )
            val id = eventsUseCases.createEventUseCase(event)
            if (item.repeat != null) {
                val repeat = item.repeat.copy(
                    eventOwnerId = id.toInt()
                )
                eventsUseCases.createRepeatUseCase(repeat)
            }
            val list = mutableListOf<Notification>()
            item.notifications.forEach {
                list.add(
                    it.copy(
                        eventOwnerId = id.toInt()
                    )
                )
            }
            eventsUseCases.createNotificationsUseCase(list)
            val att = mutableListOf<Attachment>()
            item.attachments.forEach {
                att.add(
                    it.copy(
                        eventOwnerId = id.toInt()
                    )
                )
            }
            eventsUseCases.createAttachmentsUseCase(att)
        }
    }

    private companion object {
        const val DAY_IN_MILLISECONDS = 86400000
    }

    private fun getURLForResource(month: Int): Uri {
        val resourceId = when (month) {
            Calendar.JANUARY -> R.drawable.month_january
            Calendar.FEBRUARY -> R.drawable.month_february
            Calendar.MARCH -> R.drawable.month_march
            Calendar.APRIL -> R.drawable.month_april
            Calendar.MAY -> R.drawable.month_may
            Calendar.JUNE -> R.drawable.month_june
            Calendar.JULY -> R.drawable.month_july
            Calendar.AUGUST -> R.drawable.month_august
            Calendar.SEPTEMBER -> R.drawable.month_september
            Calendar.OCTOBER -> R.drawable.month_october
            Calendar.NOVEMBER -> R.drawable.month_november
            Calendar.DECEMBER -> R.drawable.month_december
            else -> R.drawable.month_january
        }
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/drawable/" + resourceId
        )
    }

}