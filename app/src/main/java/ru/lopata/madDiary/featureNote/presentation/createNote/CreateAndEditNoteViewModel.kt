package ru.lopata.madDiary.featureNote.presentation.createNote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.madmax.madDiary.core.util.UiEvent
import ru.madmax.madDiary.featureNote.domain.model.entity.Note
import ru.madmax.madDiary.featureNote.domain.useCase.NoteUseCases
import javax.inject.Inject

@HiltViewModel
class CreateAndEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    state: SavedStateHandle
) : ViewModel() {

    private val _currentNote = MutableStateFlow(Note())
    val currentNote = _currentNote.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        state.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteByIdUseCase(noteId)?.also { note ->
                        Log.e("note", note.toString())
                        _currentNote.value = note
                    }
                }
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            if (_currentNote.value.text != "") {
                noteUseCases.createNoteUseCase(
                    Note(
                        title = _currentNote.value.title,
                        text = _currentNote.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = _currentNote.value.color,
                        noteId = _currentNote.value.noteId
                    )
                )
                _eventFlow.emit(UiEvent.Save)
            }
        }
    }

    fun noteTitleChange(newValue: String) {
        _currentNote.value = currentNote.value.copy(
            title = newValue
        )
    }

    fun noteDescriptionChange(newValue: String) {
        _currentNote.value = currentNote.value.copy(
            text = newValue
        )
    }

    fun noteColorChange(newValue: Int) {
        _currentNote.value = currentNote.value.copy(
            color = newValue
        )
    }
}