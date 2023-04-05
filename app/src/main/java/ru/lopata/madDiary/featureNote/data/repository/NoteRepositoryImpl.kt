package ru.lopata.madDiary.featureNote.data.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureNote.data.dataSource.NoteDao
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.lopata.madDiary.featureNote.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(searchQuery: String): Flow<List<NoteWithCategories>> {
        return dao.getAllNotes(searchQuery)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(noteId: Int) {
        dao.deleteNote(noteId)
    }
}