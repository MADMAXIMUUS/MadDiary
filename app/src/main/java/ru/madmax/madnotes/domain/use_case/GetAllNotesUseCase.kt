package ru.madmax.madnotes.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.domain.model.relationship.NoteWithCategories
import ru.madmax.madnotes.domain.repository.NoteRepository
import ru.madmax.madnotes.domain.util.OrderType

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