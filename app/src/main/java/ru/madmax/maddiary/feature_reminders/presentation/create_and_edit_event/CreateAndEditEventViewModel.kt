package ru.madmax.maddiary.feature_reminders.presentation.create_and_edit_event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.madmax.maddiary.core.util.UiEvent
import ru.madmax.maddiary.feature_reminders.domain.model.Event
import ru.madmax.maddiary.feature_reminders.domain.use_case.event.EventUseCases
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateAndEditEventViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _currentEvent = MutableStateFlow(Event())
    val currentEvent = _currentEvent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun updateStartTimestamp(date: Date) {
        _currentEvent.value = currentEvent.value.copy(
            startTimestamp = date
        )
    }

    fun updateEndTimestamp(date: Date) {
        _currentEvent.value = currentEvent.value.copy(
            endTimestamp = date
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

    fun updateRepeat(repeat: String) {
        _currentEvent.value = currentEvent.value.copy(
            repeat = repeat
        )
    }

    fun saveEvent() {
        viewModelScope.launch {
            if (currentEvent.value.title != "") {
                eventUseCases.createEventUseCase(currentEvent.value)
            }
            _eventFlow.emit(UiEvent.Save)
        }
    }
}