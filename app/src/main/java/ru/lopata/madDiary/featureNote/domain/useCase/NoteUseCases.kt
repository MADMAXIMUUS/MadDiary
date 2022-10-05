package ru.lopata.madDiary.featureNote.domain.useCase

data class NoteUseCases(
    val getAllNotesUseCase: GetAllNotesUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val createNoteUseCase: CreateNoteUseCase,
    val deleteNotesUseCase: DeleteNotesUseCase
)