package ru.lopata.madDiary.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.lopata.madDiary.core.data.dataSource.MadNoteDatabase
import ru.lopata.madDiary.featureNote.data.repository.NoteRepositoryImpl
import ru.lopata.madDiary.featureNote.domain.repository.NoteRepository
import ru.lopata.madDiary.featureNote.domain.useCase.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

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
            getNoteByIdUseCase = GetNoteByIdUseCase(repository),
            createNoteUseCase = CreateNoteUseCase(repository),
            deleteNotesUseCase = DeleteNotesUseCase(repository)
        )
    }
}