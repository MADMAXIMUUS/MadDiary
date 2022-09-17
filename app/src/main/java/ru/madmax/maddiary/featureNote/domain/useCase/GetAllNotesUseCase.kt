package ru.madmax.madDiary.featureNote.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.madmax.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.madmax.madDiary.featureNote.domain.repository.NoteRepository
import ru.madmax.madDiary.featureNote.domain.util.OrderType

class GetAllNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        searchQuery: String = "",
        orderType: OrderType = OrderType.Descending
    ): Flow<List<NoteWithCategories>> {
        return repository.getAllNotes(searchQuery, orderType)
    }
}