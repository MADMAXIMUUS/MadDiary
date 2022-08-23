package ru.madmax.madnotes.presentation.create_note

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.use_case.NoteUseCases
import ru.madmax.madnotes.util.NoteColors
import ru.madmax.madnotes.util.UiEvent
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
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                            _currentNote.value = currentNote.value.copy(
                                color = NoteColors.defaultDark
                            )
                        } else {
                            _currentNote.value = currentNote.value.copy(
                                color = NoteColors.defaultLight
                            )
                        }
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
                        categories = "",
                        id = _currentNote.value.id
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