package ru.madmax.maddiary.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.madmax.maddiary.core.util.Converters
import ru.madmax.maddiary.feature_note.data.data_source.NoteDao
import ru.madmax.maddiary.feature_note.domain.model.entity.Category
import ru.madmax.maddiary.feature_note.domain.model.entity.Note
import ru.madmax.maddiary.feature_note.domain.model.entity.NoteCategoryCrossRef
import ru.madmax.maddiary.feature_reminders.data.data_source.EventDao
import ru.madmax.maddiary.feature_reminders.domain.model.Event

@Database(
    entities = [
        Note::class,
        Category::class,
        Event::class,
        NoteCategoryCrossRef::class
    ], version = 5
)
@TypeConverters(Converters::class)
abstract class MadNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    abstract val eventDao: EventDao

    companion object {
        const val NAME = "MadNote_db"
    }
}