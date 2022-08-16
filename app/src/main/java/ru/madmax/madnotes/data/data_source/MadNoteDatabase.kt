package ru.madmax.madnotes.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.*
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.madmax.madnotes.domain.model.Note
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Note::class], version = 1)
abstract class MadNoteDatabase : RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val NAME = "MadNote_db"
    }
}