package ru.madmax.madnotes.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.data.data_source.NoteDatabase
import ru.madmax.madnotes.domain.model.Note

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}