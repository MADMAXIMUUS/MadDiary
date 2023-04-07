package ru.lopata.madDiary.featureNote.presentation.listNote

import ru.lopata.madDiary.featureNote.domain.model.NoteItem

data class ListNoteState(
    val notes: List<NoteItem> = emptyList()
)