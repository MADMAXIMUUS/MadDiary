package ru.lopata.madDiary.featureNote.domain.useCase

import ru.madmax.madDiary.featureNote.domain.model.entity.Note
import ru.madmax.madDiary.featureNote.domain.repository.NoteRepository

class DeleteNotesUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.deleteNote(note)
    }
}