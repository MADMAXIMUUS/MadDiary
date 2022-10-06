package ru.lopata.madDiary.featureNote.data.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureNote.data.dataSource.NoteDao
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.lopata.madDiary.featureNote.domain.repository.NoteRepository
import ru.lopata.madDiary.featureNote.domain.util.OrderType

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