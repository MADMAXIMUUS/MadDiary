package ru.lopata.madDiary.featureReminders.presentation.viewReminder

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.UiEvent
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.model.Notification
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import javax.inject.Inject

@HiltViewModel
class ViewReminderViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    state: SavedStateHandle
) : ViewModel() {
    private val _currentEvent = MutableStateFlow(ViewReminderScreenState())
    val currentEvent = _currentEvent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentRepeat = Repeat()
    private var currentNotifications = listOf<Notification>()

    init {
        state.get<Int>("eventId")?.let { eventId ->
            viewModelScope.launch {
                eventUseCases.getEventByIdUseCase(eventId)?.also { eventRepeatAttachments ->
                    val event = eventRepeatAttachments.event
                    val repeat = eventRepeatAttachments.repeat ?: Repeat()
                    val attachments = eventRepeatAttachments.attachments
                    val notifications = eventRepeatAttachments.notifications
                    currentRepeat = repeat
                    currentNotifications = notifications
                    _currentEvent.update { currentValue ->
                        currentValue.copy(
                            title = event.title,
                            completed = event.completed,
                            cover = Uri.parse(event.cover),
                            startDateTime = event.startDateTime,
                            allDay = event.allDay,
                            color = event.color,
                            location = event.location,
                            repeatEnd = repeat.repeatEnd,
                            attachments = attachments,
                            eventId = eventId
                        )
                    }
                    when (repeat.repeatInterval) {
                        Repeat.NO_REPEAT -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.never,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                        Repeat.EVERY_DAY -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.every_day,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                        Repeat.EVERY_SECOND_DAY -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.every_second_day,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                        Repeat.EVERY_WEEK -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.every_week,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                        Repeat.EVERY_SECOND_WEEK -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.every_second_week,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                        Repeat.EVERY_MONTH -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.every_month,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                        Repeat.EVERY_YEAR -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeatTitle = R.string.every_year,
                                    repeat = currentRepeat.repeatInterval
                                )
                            }
                        }
                    }
                    val titles = mutableListOf<Int>()
                    val notification = mutableListOf<Long>()
                    currentNotifications.forEach {
                        when (it.time) {
                            Notification.NEVER -> titles.add(R.string.never)
                            Notification.AT_TIME -> titles.add(R.string.at_time_of_event)
                            Notification.MINUTE_10 -> titles.add(R.string.ten_minute_before)
                            Notification.MINUTE_30 -> titles.add(R.string.thirty_minute_before)
                            Notification.HOUR -> titles.add(R.string.one_hour_before)
                            Notification.DAY -> titles.add(R.string.one_day_before)
                            Notification.WEEK -> titles.add(R.string.one_week_before)
                            Notification.MONTH -> titles.add(R.string.one_month_before)
                        }
                        notification.add(it.time)
                    }
                    _currentEvent.update { currentState ->
                        currentState.copy(
                            notifications = notification,
                            notificationTitle = titles
                        )
                    }
                }
            }
        }
        state.get<Int>("chapter")?.let { chapter ->
            _currentEvent.update { currentValue ->
                currentValue.copy(
                    chapter = chapter
                )
            }
        }
        state.get<Int>("chapters")?.let { chapters ->
            _currentEvent.update { currentValue ->
                currentValue.copy(
                    chapters = chapters
                )
            }
        }
    }

    fun deleteEvent() {
        viewModelScope.launch {
            eventUseCases.deleteEventUseCase(currentEvent.value.eventId)
            _eventFlow.emit(UiEvent.Delete)
        }
    }

    fun editEvent() {
        val bundle = Bundle()
        bundle.putParcelable(
            "eventRepeatNotificationAttachments",
            EventRepeatNotificationAttachment(
                Event(
                    title = currentEvent.value.title,
                    completed = currentEvent.value.completed,
                    startDateTime = currentEvent.value.startDateTime,
                    endDateTime = currentEvent.value.startDateTime,
                    allDay = currentEvent.value.allDay,
                    color = currentEvent.value.color,
                    cover = currentEvent.value.cover.toString(),
                    location = currentEvent.value.location,
                    note = "",
                    isAttachmentAdded = false,
                    eventId = currentEvent.value.eventId
                ),
                repeat = currentRepeat,
                attachments = currentEvent.value.attachments,
                notifications = currentNotifications
            )
        )
        viewModelScope.launch {
            _eventFlow.emit(
                UiEvent.Edit(
                    bundle
                )
            )
        }
    }
}