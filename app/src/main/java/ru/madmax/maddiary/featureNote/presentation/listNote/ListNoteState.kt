package ru.madmax.madDiary.featureNote.presentation.listNote

import ru.madmax.madDiary.featureNote.domain.model.entity.NoteModel
import ru.madmax.madDiary.featureNote.domain.util.OrderType


data class ListNoteState(
    val notes: List<NoteModel> = emptyList(),
    val orderType: OrderType = OrderType.Descending
)