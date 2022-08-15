package ru.madmax.madnotes.presentation.list_note

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.madmax.madnotes.domain.use_case.NoteUseCases
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {
    
}