package ru.lopata.madDiary.featureNote.presentation.listNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.model.entity.NoteModel
import ru.lopata.madDiary.featureNote.domain.useCase.NoteUseCases
import ru.lopata.madDiary.featureNote.domain.util.OrderType
import javax.inject.Inject

@HiltViewModel
class ListNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _listNoteState = MutableStateFlow(ListNoteState())
    val listNoteState: StateFlow<ListNoteState> = _listNoteState

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    private fun getNotes(orderType: OrderType = OrderType.Descending) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getAllNotesUseCase()
            .onEach { notesWithCategories ->
                val notes = mutableListOf<NoteModel>()
                notesWithCategories.forEach { noteWithCategories ->
                    notes.add(noteWithCategories.note.toNoteModel(noteWithCategories.categories))
                }
                _listNoteState.value = listNoteState.value.copy(
                    notes = notes.toList(),
                    orderType = orderType
                )
            }
            .launchIn(viewModelScope)
    }

    fun changeOrderType(orderType: OrderType) {
        _listNoteState.value = listNoteState.value.copy(
            orderType = orderType
        )
    }

}