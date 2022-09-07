package ru.madmax.madnotes.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.feature_note.domain.model.relationship.NoteWithCategories
import ru.madmax.madnotes.feature_note.domain.repository.NoteRepository
import ru.madmax.madnotes.feature_note.domain.util.OrderType

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