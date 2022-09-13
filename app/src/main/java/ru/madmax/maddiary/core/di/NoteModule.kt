package ru.madmax.maddiary.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.maddiary.core.data.data_source.MadNoteDatabase
import ru.madmax.maddiary.feature_note.data.repository.NoteRepositoryImpl
import ru.madmax.maddiary.feature_note.domain.repository.NoteRepository
import ru.madmax.maddiary.feature_note.domain.use_case.*
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