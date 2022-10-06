package ru.lopata.madDiary.featureNote.presentation.listNote

import ru.lopata.madDiary.featureNote.domain.model.entity.NoteModel
import ru.lopata.madDiary.featureNote.domain.util.OrderType


data class ListNoteState(
    val notes: List<NoteModel> = emptyList(),
    val orderType: OrderType = OrderType.Descending
)