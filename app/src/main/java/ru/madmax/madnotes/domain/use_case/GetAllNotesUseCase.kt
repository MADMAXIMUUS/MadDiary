package ru.madmax.madnotes.domain.use_case

import ru.madmax.madnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.madmax.madnotes.domain.repository.NoteRepository
import ru.madmax.madnotes.domain.util.OrderType

class GetAllNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(orderType: OrderType = OrderType.Descending): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (orderType) {
                is OrderType.Ascending -> notes.sortedBy { it.timestamp }
                is OrderType.Descending -> notes.sortedByDescending { it.timestamp }
            }
        }
    }
}