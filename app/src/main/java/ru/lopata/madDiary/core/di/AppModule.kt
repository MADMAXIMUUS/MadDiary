package ru.lopata.madDiary.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.madDiary.core.data.dataSource.MIGRATION_3_4
import ru.madmax.madDiary.core.data.dataSource.MIGRATION_4_5
import ru.madmax.madDiary.core.data.dataSource.MadNoteDatabase
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