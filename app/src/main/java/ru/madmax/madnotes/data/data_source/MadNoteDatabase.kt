package ru.madmax.madnotes.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.madmax.madnotes.domain.model.Category
import ru.madmax.madnotes.domain.model.Note

@Database(
    entities = [
        Note::class,
        Category::class
    ], version = 1
)
abstract class MadNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val NAME = "MadNote_db"
    }
}