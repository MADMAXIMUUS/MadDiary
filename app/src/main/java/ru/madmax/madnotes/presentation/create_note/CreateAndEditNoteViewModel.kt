package ru.madmax.madnotes.presentation.create_note

import android.graphics.Color
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.use_case.NoteUseCases
import ru.madmax.madnotes.util.UiEvent
import javax.inject.Inject

@HiltViewModel
class CreateAndEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    state: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf("")
    val noteTitle: State<String> = _noteTitle

    private val _noteDescription = mutableStateOf("")
    val noteDescription: State<String> = _noteDescription

    private val _noteColor = mutableStateOf(Color.parseColor("#FFF5F5F5"))
    val noteColor: State<Int> = _noteColor

    private var currentNoteId: Int? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        state.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch(Dispatchers.IO) {
                    noteUseCases.getNoteByIdUseCase(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = note.title
                        _noteDescription.value = note.text
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            if (_noteDescription.value != "") {
                noteUseCases.createNoteUseCase(
                    Note(
                        title = _noteTitle.value,
                        text = _noteDescription.value,
                        timestamp = System.currentTimeMillis(),
                        color = _noteColor.value,
                        categories = "",
                        id = currentNoteId
                    )
                )
                _eventFlow.emit(UiEvent.Save)
            }
        }
    }

    fun noteTitleChange(newValue: String) {
        _noteTitle.value = newValue
    }

    fun noteDescriptionChange(newValue: String) {
        _noteDescription.value = newValue
    }

    fun noteColorChange(newValue: Int) {
        _noteColor.value = newValue
    }
}