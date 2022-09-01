package ru.madmax.madnotes.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.madmax.madnotes.domain.model.entity.Category
import ru.madmax.madnotes.domain.model.entity.Note
import ru.madmax.madnotes.domain.model.entity.NoteCategoryCrossRef

@Database(
    entities = [
        Note::class,
        Category::class,
        NoteCategoryCrossRef::class
    ], version = 2
)
abstract class MadNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val NAME = "MadNote_db"
    }
}