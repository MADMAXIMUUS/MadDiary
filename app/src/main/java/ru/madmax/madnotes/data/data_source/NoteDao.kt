package ru.madmax.madnotes.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.domain.model.Note


@Dao
interface NoteDao {

    @Query("SELECT * FROM NOTES")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM NOTES WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)


}