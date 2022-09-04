package ru.madmax.madnotes.feature_note.presentation.list_note

import ru.madmax.madnotes.domain.model.entity.NoteModel
import ru.madmax.madnotes.domain.util.OrderType

data class ListNoteState(
    val notes: List<NoteModel> = emptyList(),
    val orderType: OrderType = OrderType.Descending
)