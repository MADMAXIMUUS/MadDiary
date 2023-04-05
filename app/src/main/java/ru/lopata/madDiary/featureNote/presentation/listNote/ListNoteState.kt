package ru.lopata.madDiary.featureNote.presentation.listNote

import ru.lopata.madDiary.featureNote.domain.model.entity.Note

data class ListNoteState(
    val notes: List<Note> = emptyList()
)