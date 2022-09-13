package ru.madmax.maddiary.feature_note.data.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.maddiary.feature_note.data.data_source.NoteDao
import ru.madmax.maddiary.feature_note.domain.model.entity.Note
import ru.madmax.maddiary.feature_note.domain.model.relationship.NoteWithCategories
import ru.madmax.maddiary.feature_note.domain.repository.NoteRepository
import ru.madmax.maddiary.feature_note.domain.util.OrderType

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(searchQuery: String, orderType: OrderType): Flow<List<NoteWithCategories>> {
        return dao.getNotesSorted(searchQuery, orderType)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}