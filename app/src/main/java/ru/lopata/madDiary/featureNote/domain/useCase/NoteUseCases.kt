package ru.lopata.madDiary.featureNote.domain.useCase

data class NoteUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val createNoteUseCase: CreateNoteUseCase,
    val deleteNotesUseCase: DeleteNotesUseCase
)