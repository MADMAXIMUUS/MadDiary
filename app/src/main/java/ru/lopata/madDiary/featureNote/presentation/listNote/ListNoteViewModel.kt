package ru.lopata.madDiary.featureNote.presentation.listNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.lopata.madDiary.core.util.toDate
import ru.lopata.madDiary.featureNote.domain.model.NoteItem
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.useCase.NoteUseCases
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _listNoteState = MutableStateFlow(ListNoteState())
    val listNoteState: StateFlow<ListNoteState> = _listNoteState

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUseCase()
            .onEach { notesWithCategories ->
                val notes = mutableListOf<NoteItem>()
                notesWithCategories.forEach { noteWithCategories ->
                    notes.add(
                        NoteItem(
                            noteId = noteWithCategories.note.noteId ?: -1,
                            text = noteWithCategories.note.text,
                            title = noteWithCategories.note.title,
                            pinned = noteWithCategories.note.pinned,
                            timestamp = noteWithCategories.note.timestamp.toDate(),
                            categories = noteWithCategories.categories
                        )
                    )
                }
                _listNoteState.value = listNoteState.value.copy(
                    notes = notes.toList()
                )
            }
            .launchIn(viewModelScope)
    }

    fun undoDelete(note: Note) {
        viewModelScope.launch {
            noteUseCases.createNoteUseCase(note.copy(noteId = null))
        }
    }

}