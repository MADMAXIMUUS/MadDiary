package ru.madmax.madDiary.featureNote.domain.useCase

import ru.madmax.madDiary.featureNote.domain.model.entity.Note
import ru.madmax.madDiary.featureNote.domain.repository.NoteRepository

class CreateNoteUseCase (
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.insertNote(note)
    }
}