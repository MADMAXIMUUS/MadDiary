package ru.madmax.madnotes.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.domain.util.OrderType


@Dao
interface NoteDao {

    fun getNotesSorted(searchQuery: String, orderType: OrderType): Flow<List<Note>> =
        when (orderType) {
            OrderType.Ascending -> getAllNotesAsc(searchQuery)
            OrderType.Descending -> getAllNotesDesc(searchQuery)
        }

    @Query("SELECT * FROM NOTES WHERE title LIKE '%' || :searchQuery || '%' ORDER BY pinned DESC, timestamp ASC")
    fun getAllNotesAsc(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM NOTES WHERE title LIKE '%' || :searchQuery || '%' ORDER BY pinned DESC, timestamp DESC")
    fun getAllNotesDesc(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM NOTES WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}