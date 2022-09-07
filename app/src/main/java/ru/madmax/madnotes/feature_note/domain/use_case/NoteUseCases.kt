package ru.madmax.madnotes.feature_note.domain.use_case

data class NoteUseCases(
    val getAllNotesUseCase: GetAllNotesUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase,
    val createNoteUseCase: CreateNoteUseCase,
    val deleteNotesUseCase: DeleteNotesUseCase
)