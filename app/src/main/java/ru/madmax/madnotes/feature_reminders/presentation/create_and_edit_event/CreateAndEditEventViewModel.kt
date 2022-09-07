package ru.madmax.madnotes.feature_reminders.presentation.create_and_edit_event

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.madmax.madnotes.core.util.UiEvent
import ru.madmax.madnotes.feature_reminders.domain.model.Event
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateAndEditEventViewModel @Inject constructor(
    state: SavedStateHandle
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
}