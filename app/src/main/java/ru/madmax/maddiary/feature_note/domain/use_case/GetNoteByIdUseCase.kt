package ru.madmax.maddiary.feature_note.domain.use_case

import ru.madmax.maddiary.feature_note.domain.model.entity.Note
import ru.madmax.maddiary.feature_note.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}