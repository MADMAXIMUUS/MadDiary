package ru.madmax.madnotes.domain.use_case

import ru.madmax.madnotes.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.getNoteById(id)
    }
}