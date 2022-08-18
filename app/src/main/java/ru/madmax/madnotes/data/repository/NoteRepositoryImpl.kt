package ru.madmax.madnotes.data.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.data.data_source.NoteDao
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.repository.NoteRepository
import ru.madmax.madnotes.domain.util.OrderType

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(searchQuery: String, orderType: OrderType): Flow<List<Note>> {
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