package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import android.icu.util.Calendar
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.EditTextState
import ru.lopata.madDiary.core.util.UiEvent
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventAndRepeat
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class CreateAndEditEventViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    state: SavedStateHandle
) : ViewModel() {

    private val _currentEvent = MutableStateFlow(CreateEventScreenState())
    val currentEvent = _currentEvent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var preEditEvent: Event = Event()
    private var preEditRepeat: Repeat = Repeat()

    init {
        state.get<EventAndRepeat>("eventAndRepeat")?.let { eventAndRepeat ->
            viewModelScope.launch {
                val event = eventAndRepeat.event
                preEditEvent = event
                val repeat = eventAndRepeat.repeat ?: Repeat()
                preEditRepeat = repeat
                _currentEvent.update { currentValue ->
                    currentValue.copy(
                        title = EditTextState(
                            text = event.title,
                            isEmpty = false,
                            isError = false
                        ),
                        completed = event.completed,
                        startDateTime = event.startDateTime,
                        endDateTime = event.endDateTime,
                        allDay = event.allDay,
                        color = event.color,
                        location = event.location,
                        note = event.note,
                        repeat = repeat.repeatInterval,
                        repeatEnd = repeat.repeatEnd,
                        id = event.eventId
                    )
                }
                when (repeat.repeatInterval) {
                    Repeat.NO_REPEAT -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.never
                            )
                        }
                    }
                    Repeat.EVERY_DAY -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_day
                            )
                        }
                    }
                    Repeat.EVERY_SECOND_DAY -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_second_day
                            )
                        }
                    }
                    Repeat.EVERY_WEEK -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_week
                            )
                        }
                    }
                    Repeat.EVERY_SECOND_WEEK -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_second_week
                            )
                        }
                    }
                    Repeat.EVERY_MONTH -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_month
                            )
                        }
                    }
                    Repeat.EVERY_YEAR -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_year
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateStartTimestamp(date: Date) {
        _currentEvent.value = currentEvent.value.copy(
            startDateTime = date,
            isStartDateError = false
        )
    }

    fun updateEndTimestamp(date: Date) {
        _currentEvent.value = currentEvent.value.copy(
            endDateTime = date,
            isEndDateError = false
        )
    }

    fun updateNote(note: String) {
        _currentEvent.value = currentEvent.value.copy(
            note = note
        )
    }

    fun updateAllDayState(state: Boolean) {
        if (state) {
            val calendarStart = Calendar.getInstance()
            calendarStart.timeInMillis = currentEvent.value.startDateTime.time
            calendarStart.apply {
                set(Calendar.HOUR, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                set(Calendar.MILLISECONDS_IN_DAY, 0)
            }
            val calendarEnd = Calendar.getInstance()
            calendarEnd.timeInMillis = currentEvent.value.endDateTime.time
            calendarEnd.apply {
                set(Calendar.HOUR, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                set(Calendar.MILLISECONDS_IN_DAY, 0)
            }
            _currentEvent.value = currentEvent.value.copy(
                startDateTime = Date(calendarStart.timeInMillis),
                endDateTime = Date(calendarEnd.timeInMillis),
            )
        }
        _currentEvent.value = currentEvent.value.copy(
            allDay = state
        )
    }

    fun updateRepeat(repeat: Long) {
        _currentEvent.value = currentEvent.value.copy(
            repeat = repeat
        )
    }

    fun updateRepeatTitle(@StringRes title: Int) {
        _currentEvent.value = currentEvent.value.copy(
            repeatTitle = title
        )
    }

    fun updateTitle(title: String, start: Int) {
        var position = 0
        val currentText = currentEvent.value.title.text
        if (title != currentText) {
            /*for (i in 0 until max(title.length, currentText.length)) {
                if (i == title.length || i == currentText.length) {
                        position = title.length
                    break
                }
                if (title[i] != currentText[i]) {
                    position = i
                    break
                }
            }*/
            _currentEvent.value = currentEvent.value.copy(
                title = currentEvent.value.title.copy(
                    text = title,
                    isEmpty = title.isEmpty(),
                    cursorPosition = start
                )
            )
            if (title.isNotEmpty()) {
                _currentEvent.value = currentEvent.value.copy(
                    title = currentEvent.value.title.copy(
                        isError = false
                    )
                )
            }
        }
    }

    fun deleteEvent() {
        if (preEditEvent != Event()) {
            viewModelScope.launch {
                eventUseCases.deleteEventUseCase(
                    preEditEvent
                )
                _eventFlow.emit(UiEvent.Delete)
            }
        }
    }

    fun saveEvent() {
        viewModelScope.launch {
            if (!currentEvent.value.title.isEmpty
                && currentEvent.value.startDateTime != Date(0)
                && currentEvent.value.endDateTime != Date(0)
            ) {
                if (currentEvent.value.id == null) {
                    val id = eventUseCases.createEventUseCase(
                        Event(
                            title = currentEvent.value.title.text,
                            startDateTime = currentEvent.value.startDateTime,
                            endDateTime = currentEvent.value.endDateTime,
                            allDay = currentEvent.value.allDay,
                            color = currentEvent.value.color,
                            completed = currentEvent.value.completed,
                            location = currentEvent.value.location,
                            note = currentEvent.value.note,
                        )
                    )
                    eventUseCases.createRepeatUseCase(
                        Repeat(
                            repeatStart = currentEvent.value.startDateTime,
                            repeatInterval = currentEvent.value.repeat,
                            eventOwnerId = id.toInt()
                        )
                    )
                } else {
                    eventUseCases.createEventUseCase(
                        Event(
                            eventId = currentEvent.value.id,
                            title = currentEvent.value.title.text,
                            startDateTime = currentEvent.value.startDateTime,
                            endDateTime = currentEvent.value.endDateTime,
                            allDay = currentEvent.value.allDay,
                            color = currentEvent.value.color,
                            completed = currentEvent.value.completed,
                            location = currentEvent.value.location,
                            note = currentEvent.value.note,
                        )
                    )
                    eventUseCases.createRepeatUseCase(
                        Repeat(
                            repeatStart = currentEvent.value.startDateTime,
                            repeatInterval = currentEvent.value.repeat,
                            eventOwnerId = currentEvent.value.id!!
                        )
                    )
                }
                _eventFlow.emit(UiEvent.Save)
            } else {
                if (currentEvent.value.title.isEmpty) {
                    _currentEvent.value = currentEvent.value.copy(
                        title = currentEvent.value.title.copy(isError = true)
                    )
                }
                if (currentEvent.value.startDateTime == Date(0)) {
                    _currentEvent.value = currentEvent.value.copy(
                        isStartDateError = true
                    )
                }
                if (currentEvent.value.startDateTime == Date(0)) {
                    _currentEvent.value = currentEvent.value.copy(
                        isEndDateError = true
                    )
                }
            }
        }
    }
}