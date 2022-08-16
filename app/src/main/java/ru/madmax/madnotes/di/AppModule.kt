package ru.madmax.madnotes.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.madnotes.data.data_source.MadNoteDatabase
import ru.madmax.madnotes.data.repository.NoteRepositoryImpl
import ru.madmax.madnotes.domain.repository.NoteRepository
import ru.madmax.madnotes.domain.use_case.DeleteNotesUseCase
import ru.madmax.madnotes.domain.use_case.GetAllNotesUseCase
import ru.madmax.madnotes.domain.use_case.NoteUseCases
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
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(db: MadNoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotesUseCase = GetAllNotesUseCase(repository),
            deleteNotesUseCase = DeleteNotesUseCase(repository)
        )
    }

}