package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.lopata.madDiary.core.util.UiEvent
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class CreateAndEditEventViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _currentEvent = MutableStateFlow(CreateEventScreenState())
    val currentEvent = _currentEvent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
        _currentEvent.value = currentEvent.value.copy(
            allDay = state
        )
    }

    fun updateRepeat(repeat: Long) {
        _currentEvent.value = currentEvent.value.copy(
            repeat = repeat
        )
    }

    fun updateRepeatTitle(@StringRes title: Int){
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

    fun saveEvent() {
        viewModelScope.launch {
            if (!currentEvent.value.title.isEmpty
                && currentEvent.value.startDateTime != Date(0)
                && currentEvent.value.endDateTime != Date(0)
            ) {
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
                        notification = currentEvent.value.notification,
                        attachment = currentEvent.value.attachment
                    )
                )
                eventUseCases.createRepeatUseCase(
                    Repeat(
                        repeatStart = currentEvent.value.startDateTime,
                        repeatInterval = currentEvent.value.repeat,
                        eventOwnerId = id.toInt()
                    )
                )
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