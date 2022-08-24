package ru.madmax.madnotes.domain.use_case

import ru.madmax.madnotes.domain.model.entity.Note
import ru.madmax.madnotes.domain.repository.NoteRepository

class DeleteNotesUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.deleteNote(note)
    }
}