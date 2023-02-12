package ru.lopata.madDiary.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.SyncStateContract
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.lopata.madDiary.core.data.dataSource.*
import ru.lopata.madDiary.core.util.SETTINGS
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
        ).addMigrations(
            MIGRATION_3_4,
            MIGRATION_4_5,
            MIGRATION_5_6,
            MIGRATION_6_7,
            MIGRATION_7_8,
            MIGRATION_8_9,
            MIGRATION_9_10,
            MIGRATION_10_11,
            MIGRATION_11_12,
            MIGRATION_12_13,
            MIGRATION_13_14,
            MIGRATION_14_15,
            MIGRATION_15_16,
            MIGRATION_16_17
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            SETTINGS,
            Context.MODE_PRIVATE
        )
    }
}