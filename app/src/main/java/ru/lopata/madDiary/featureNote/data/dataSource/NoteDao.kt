package ru.lopata.madDiary.featureNote.data.dataSource

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.model.relationship.NoteWithCategories

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM NOTES WHERE title LIKE '%' || :searchQuery || '%' ORDER BY pinned DESC, timestamp DESC")
    fun getAllNotes(searchQuery: String): Flow<List<NoteWithCategories>>

    @Query("SELECT * FROM NOTES WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM NOTES WHERE noteId = :noteId")
    suspend fun deleteNote(noteId: Int)

}