package ru.madmax.maddiary.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.maddiary.core.data.data_source.MIGRATION_3_4
import ru.madmax.maddiary.core.data.data_source.MIGRATION_4_5
import ru.madmax.maddiary.core.data.data_source.MadNoteDatabase
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
        ).addMigrations(MIGRATION_3_4, MIGRATION_4_5)
            .build()
    }

}