package ru.madmax.madnotes.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.madnotes.data.data_source.NoteDao
import ru.madmax.madnotes.data.data_source.NoteDatabase
import ru.madmax.madnotes.data.repository.NoteRepositoryImpl
import ru.madmax.madnotes.domain.model.Note
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
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(db: NoteDatabase): NoteRepository {
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