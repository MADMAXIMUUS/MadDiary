package ru.madmax.madnotes.presentation.list_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.use_case.NoteUseCases
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _listNoteState = MutableStateFlow(emptyList<Note>())
    val listNoteState: StateFlow<List<Note>> = _listNoteState

    val notes = noteUseCases.getAllNotesUseCase().asLiveData()

    init {

    }

}