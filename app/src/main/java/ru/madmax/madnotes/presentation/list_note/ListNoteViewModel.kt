package ru.madmax.madnotes.presentation.list_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.use_case.NoteUseCases
import ru.madmax.madnotes.domain.util.OrderType
import ru.madmax.madnotes.util.UiState
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _listNoteState = MutableStateFlow<UiState<List<Note>>>(UiState.Loading)
    var listNoteState: StateFlow<UiState<List<Note>>> = _listNoteState

    init {
        getNotes(OrderType.Descending)
    }

    private fun getNotes(orderType: OrderType) {
        _listNoteState.value = UiState.Loading
        viewModelScope.launch {
            noteUseCases.getAllNotesUseCase().collect {
                _listNoteState.value = UiState.Success(it)
            }
        }
    }

}