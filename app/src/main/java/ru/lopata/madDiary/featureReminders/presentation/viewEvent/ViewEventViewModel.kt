package ru.lopata.madDiary.featureReminders.presentation.viewEvent

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
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatAttachment
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import javax.inject.Inject

@HiltViewModel
class ViewEventViewModel @Inject constructor(
    val eventUseCases: EventUseCases,
    val state: SavedStateHandle
) : ViewModel() {
    private val _currentEvent = MutableStateFlow(ViewEventScreenState())
    val currentEvent = _currentEvent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentRepeat = Repeat()

    init {
        state.get<Int>("eventId")?.let { eventId ->
            viewModelScope.launch {
                eventUseCases.getEventByIdUseCase(eventId)?.also { eventRepeatAttachments ->
                    val event = eventRepeatAttachments.event
                    val repeat = eventRepeatAttachments.repeat ?: Repeat()
                    val attachments = eventRepeatAttachments.attachments
                    currentRepeat = repeat
                    _currentEvent.update { currentValue ->
                        currentValue.copy(
                            title = event.title,
                            completed = event.completed,
                            cover = Uri.parse(event.cover),
                            startDateTime = event.startDateTime,
                            endDateTime = event.endDateTime,
                            allDay = event.allDay,
                            color = event.color,
                            location = event.location,
                            note = event.note,
                            repeatEnd = repeat.repeatEnd,
                            attachments = attachments,
                            eventId = eventId
                        )
                    }
                    when (repeat.repeatInterval) {
                        Repeat.NO_REPEAT -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.never
                                )
                            }
                        }
                        Repeat.EVERY_DAY -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.every_day
                                )
                            }
                        }
                        Repeat.EVERY_SECOND_DAY -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.every_second_day
                                )
                            }
                        }
                        Repeat.EVERY_WEEK -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.every_week
                                )
                            }
                        }
                        Repeat.EVERY_SECOND_WEEK -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.every_second_week
                                )
                            }
                        }
                        Repeat.EVERY_MONTH -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.every_month
                                )
                            }
                        }
                        Repeat.EVERY_YEAR -> {
                            _currentEvent.update { currentValue ->
                                currentValue.copy(
                                    repeat = R.string.every_year
                                )
                            }
                        }
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
            eventUseCases.deleteEventUseCase(
                Event(
                    title = currentEvent.value.title,
                    completed = currentEvent.value.completed,
                    startDateTime = currentEvent.value.startDateTime,
                    endDateTime = currentEvent.value.endDateTime,
                    allDay = currentEvent.value.allDay,
                    color = currentEvent.value.color,
                    cover = currentEvent.value.cover.toString(),
                    location = currentEvent.value.location,
                    note = currentEvent.value.note,
                    isAttachmentAdded = false,
                    eventId = currentEvent.value.eventId
                )
            )
            _eventFlow.emit(UiEvent.Delete)
        }
    }

    fun editEvent() {
        val bundle = Bundle()
        bundle.putParcelable(
            "eventRepeatAttachments",
            EventRepeatAttachment(
                Event(
                    title = currentEvent.value.title,
                    completed = currentEvent.value.completed,
                    startDateTime = currentEvent.value.startDateTime,
                    endDateTime = currentEvent.value.endDateTime,
                    allDay = currentEvent.value.allDay,
                    color = currentEvent.value.color,
                    cover = currentEvent.value.cover.toString(),
                    location = currentEvent.value.location,
                    note = currentEvent.value.note,
                    isAttachmentAdded = false,
                    eventId = currentEvent.value.eventId
                ),
                repeat = currentRepeat,
                attachments = currentEvent.value.attachments
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