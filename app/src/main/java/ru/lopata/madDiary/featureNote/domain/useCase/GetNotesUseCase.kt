package ru.lopata.madDiary.featureNote.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.lopata.madDiary.featureNote.domain.repository.NoteRepository

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(searchQuery: String=""): Flow<List<NoteWithCategories>> {
        return repository.getAllNotes(searchQuery)
    }
}