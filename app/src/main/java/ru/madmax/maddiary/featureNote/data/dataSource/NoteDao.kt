package ru.madmax.madDiary.featureNote.data.dataSource

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.madmax.madDiary.featureNote.domain.model.entity.Note
import ru.madmax.madDiary.featureNote.domain.model.relationship.NoteWithCategories
import ru.madmax.madDiary.featureNote.domain.util.OrderType


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