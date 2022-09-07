package ru.madmax.madnotes.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.madmax.madnotes.core.util.Converters
import ru.madmax.madnotes.feature_note.data.data_source.NoteDao
import ru.madmax.madnotes.feature_note.domain.model.entity.Category
import ru.madmax.madnotes.feature_note.domain.model.entity.Note
import ru.madmax.madnotes.feature_note.domain.model.entity.NoteCategoryCrossRef
import ru.madmax.madnotes.feature_reminders.data.data_source.EventDao
import ru.madmax.madnotes.feature_reminders.domain.model.Event

@Database(
    entities = [
        Note::class,
        Category::class,
        Event::class,
        NoteCategoryCrossRef::class
    ], version = 3
)
@TypeConverters(Converters::class)
abstract class MadNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    abstract val eventDao: EventDao

    companion object {
        const val NAME = "MadNote_db"
    }
}