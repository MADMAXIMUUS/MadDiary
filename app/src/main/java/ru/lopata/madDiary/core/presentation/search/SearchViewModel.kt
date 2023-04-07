package ru.lopata.madDiary.core.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.lopata.madDiary.featureNote.domain.useCase.NoteUseCases
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.useCase.EventUseCases
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases, private val eventsUseCases: EventUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    fun search() {
        val searchQuery = uiState.value.searchQuery
        if (searchQuery.isEmpty()) return
        job?.cancel()
        job = viewModelScope.launch {
            combine(
                noteUseCases.getNotesUseCase(searchQuery),
                eventsUseCases.getEventsUseCase(searchQuery)
            ) { notes, events ->
                notes.map(NoteWithCategories::toNoteItem) +
                        events.map(EventRepeatNotificationAttachment::toEventItem)
            }.collectLatest {
                _uiState.update { currentState ->
                    currentState.copy(
                        searchResults = it
                    )
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query
            )
        }
    }

    fun updateEventState(id: Int, state: Boolean) {
        viewModelScope.launch {
            eventsUseCases.getEventByIdUseCase(id)?.let {
                val event = it.event.copy(completed = state)
                eventsUseCases.updateEventUseCase(event)
            }
        }
    }
}