package ru.lopata.madDiary.core.data.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.madmax.madDiary.core.util.Converters
import ru.madmax.madDiary.featureNote.data.dataSource.NoteDao
import ru.madmax.madDiary.featureNote.domain.model.entity.Category
import ru.madmax.madDiary.featureNote.domain.model.entity.Note
import ru.madmax.madDiary.featureNote.domain.model.entity.NoteCategoryCrossRef
import ru.madmax.madDiary.featureReminders.data.dataSource.EventDao
import ru.madmax.madDiary.featureReminders.domain.model.Event

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