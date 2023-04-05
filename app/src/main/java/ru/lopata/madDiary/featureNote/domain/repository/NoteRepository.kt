package ru.lopata.madDiary.featureNote.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories
interface NoteRepository {

    fun getAllNotes(searchQuery: String): Flow<List<NoteWithCategories>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(noteId: Int)
}