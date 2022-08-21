package ru.madmax.madnotes.presentation.create_note

import android.graphics.Color
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.madmax.madnotes.domain.use_case.NoteUseCases

class CreateAndEditNoteViewModel(
    private val noteUseCases: NoteUseCases,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf("")
    var noteTitle: State<String> = _noteTitle

    private val _noteDescription = mutableStateOf("")
    var noteDescription: State<String> = _noteDescription

    private val _noteColor = mutableStateOf(Color.parseColor("#FFF5F5F5"))
    var noteColor: State<Int> = _noteColor

    private var currentNoteId: Int? = null

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