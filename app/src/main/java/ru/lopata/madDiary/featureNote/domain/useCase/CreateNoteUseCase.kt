package ru.lopata.madDiary.featureNote.domain.useCase

import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.repository.NoteRepository

class CreateNoteUseCase (
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.insertNote(note)
    }
}