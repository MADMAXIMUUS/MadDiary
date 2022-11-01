package ru.lopata.madDiary.core.data.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.lopata.madDiary.core.util.Converters
import ru.lopata.madDiary.featureNote.data.dataSource.NoteDao
import ru.lopata.madDiary.featureNote.domain.model.entity.Category
import ru.lopata.madDiary.featureNote.domain.model.entity.Note
import ru.lopata.madDiary.featureNote.domain.model.entity.NoteCategoryCrossRef
import ru.lopata.madDiary.featureReminders.data.dataSource.EventDao
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.Repeat

@Database(
    entities = [
        Note::class,
        Category::class,
        Event::class,
        Repeat::class,
        NoteCategoryCrossRef::class
    ], version = 10
)
@TypeConverters(Converters::class)
abstract class MadNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    abstract val eventDao: EventDao

    companion object {
        const val NAME = "MadNote_db"
    }
}