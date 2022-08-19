package ru.madmax.madnotes.domain.use_case

data class NoteUseCases(
    val getAllNotesUseCase: GetAllNotesUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val createNoteUseCase: CreateNoteUseCase,
    val deleteNotesUseCase: DeleteNotesUseCase
)