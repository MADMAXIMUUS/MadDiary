package ru.lopata.madDiary.featureNote.domain.useCase

import ru.lopata.madDiary.featureNote.domain.repository.NoteRepository

class DeleteNotesUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}