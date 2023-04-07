package ru.lopata.madDiary.core.presentation.search

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
import ru.lopata.madDiary.featureNote.domain.model.NoteItem
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.lopata.madDiary.featureNote.domain.useCase.NoteUseCases
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.useCase.EventUseCases
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.EventItem
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val eventUseCases: EventUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    fun search() {
        val searchQuery = uiState.value.searchQuery
        if (searchQuery.isEmpty()) return
        val list = mutableListOf<DelegateAdapterItem>()
        job?.cancel()
        job = merge(
            noteUseCases.getAllNotesUseCase(searchQuery),
            eventUseCases.getEventsUseCase(searchQuery)
        ).onEach { flowElements ->
            flowElements.forEach {
                if (it is NoteWithCategories) {
                    list.add(
                        NoteItem(
                            noteId = it.note.noteId ?: -1,
                            title = it.note.title,
                            text = it.note.text,
                            color = it.note.color,
                            timestamp = it.note.timestamp.toDate(),
                            pinned = it.note.pinned,
                            categories = it.categories
                        )
                    )
                }
                if (it is EventRepeatNotificationAttachment) {
                    list.add(
                        EventItem(
                            id = it.event.eventId ?: -1,
                            chapter = 0,
                            chapters = 0,
                            title = it.event.title,
                            pass = false,
                            type = it.event.type,
                            subtitleFrom = when (it.event.type) {
                                Event.Types.EVENT -> {
                                    R.string.from
                                }
                                Event.Types.TASK -> {
                                    R.string.reminder_task_deadline
                                }
                                Event.Types.REMINDER -> {
                                    R.string.reminder_reminder_date
                                }
                            },
                            startTime = it.event.startDateTime.time.toTimeZone(),
                            subtitleTo = when (it.event.type) {
                                Event.Types.EVENT -> {
                                    R.string.to
                                }
                                Event.Types.TASK, Event.Types.REMINDER -> {
                                    -1
                                }
                            },
                            endTime = it.event.endDateTime.time.toTimeZone(),
                            address = it.event.location,
                            color = it.event.color,
                            isNotificationSet = it.notifications.isNotEmpty(),
                            isAttachmentAdded = it.attachments.isNotEmpty(),
                            cover = Uri.parse(it.event.cover),
                            isChecked = it.event.completed
                        )
                    )
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        searchResults = list.toList()
                    )
                }
            }
        }
            .launchIn(viewModelScope)
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
            eventUseCases.getEventByIdUseCase(id)?.let {
                val event = it.event.copy(completed = state)
                eventUseCases.createEventUseCase(event)
            }
        }
    }
}