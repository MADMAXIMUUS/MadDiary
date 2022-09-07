package ru.madmax.madnotes.feature_note.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.feature_note.domain.model.entity.Note
import ru.madmax.madnotes.feature_note.domain.model.relationship.NoteWithCategories
import ru.madmax.madnotes.feature_note.domain.util.OrderType


@Dao
interface NoteDao {

    fun getNotesSorted(searchQuery: String, orderType: OrderType): Flow<List<NoteWithCategories>> =
        when (orderType) {
            OrderType.Ascending -> getAllNotesAsc(searchQuery)
            OrderType.Descending -> getAllNotesDesc(searchQuery)
        }

    @Transaction
    @Query("SELECT * FROM NOTES WHERE title LIKE '%' || :searchQuery || '%' ORDER BY pinned DESC, timestamp ASC")
    fun getAllNotesAsc(searchQuery: String): Flow<List<NoteWithCategories>>

    @Transaction
    @Query("SELECT * FROM NOTES WHERE title LIKE '%' || :searchQuery || '%' ORDER BY pinned DESC, timestamp DESC")
    fun getAllNotesDesc(searchQuery: String): Flow<List<NoteWithCategories>>

    @Query("SELECT * FROM NOTES WHERE noteId = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}