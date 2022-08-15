package ru.madmax.madnotes.domain.use_case

data class NoteUseCases(
    val getAllNotesUseCase: GetAllNotesUseCase,
    val deleteNotesUseCase: DeleteNotesUseCase
)