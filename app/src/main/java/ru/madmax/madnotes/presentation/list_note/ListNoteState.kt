package ru.madmax.madnotes.presentation.list_note

import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.util.OrderType

data class ListNoteState(
    val notes: List<Note> = emptyList(),
    val orderType: OrderType = OrderType.Descending
)