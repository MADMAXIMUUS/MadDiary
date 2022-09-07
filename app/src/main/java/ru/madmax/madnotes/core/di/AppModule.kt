package ru.madmax.madnotes.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.madnotes.core.data.data_source.MadNoteDatabase
import ru.madmax.madnotes.core.util.Converters
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMadNoteDatabase(
        app: Application
    ): MadNoteDatabase {
        return Room.databaseBuilder(
            app,
            MadNoteDatabase::class.java,
            MadNoteDatabase.NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

}