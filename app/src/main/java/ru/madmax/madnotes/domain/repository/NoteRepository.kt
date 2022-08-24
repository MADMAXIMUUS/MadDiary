package ru.madmax.madnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.domain.model.entity.Note
import ru.madmax.madnotes.domain.model.relationship.NoteWithCategories
import ru.madmax.madnotes.domain.util.OrderType

interface NoteRepository {

    fun getAllNotes(searchQuery: String, orderType: OrderType): Flow<List<NoteWithCategories>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}