package ru.madmax.madnotes.domain.use_case

import ru.madmax.madnotes.domain.model.entity.Note
import ru.madmax.madnotes.domain.repository.NoteRepository

class CreateNoteUseCase (
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repository.insertNote(note)
    }
}