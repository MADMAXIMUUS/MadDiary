package ru.madmax.madnotes.presentation.create_note

import android.graphics.Color
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.madmax.madnotes.domain.use_case.NoteUseCases

class CreateAndEditNoteViewModel(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _noteTitle = mutableStateOf("")
    var noteTitle: State<String> = _noteTitle

    private val _noteDescription = mutableStateOf("")
    var noteDescription: State<String> = _noteDescription

    private val _noteColor = mutableStateOf(Color.parseColor("#FFF5F5F5"))
    var noteColor: State<Int> = _noteColor

    init {
        viewModelScope.launch(Dispatchers.IO) {
           // val note = noteUseCases.getNoteByIdUseCase()
        }
    }
}