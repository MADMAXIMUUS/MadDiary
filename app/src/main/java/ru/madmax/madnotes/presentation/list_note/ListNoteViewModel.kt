package ru.madmax.madnotes.presentation.list_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.use_case.NoteUseCases
import ru.madmax.madnotes.domain.util.OrderType
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _listNoteState = mutableStateOf(ListNoteState())
    val listNoteState: State<ListNoteState> = _listNoteState

    private var recentlyDeletedNote: Note? = null

    init {
        getNotes()
    }

    private fun getNotes(orderType: OrderType = OrderType.Descending) {
        viewModelScope.launch {
            noteUseCases.getAllNotesUseCase().collect {
                _listNoteState.value = listNoteState.value.copy(
                    notes = it,
                    orderType = orderType
                )
            }
        }
    }

}