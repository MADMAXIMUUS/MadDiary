package ru.lopata.madDiary.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.lopata.madDiary.core.data.dataSource.MadNoteDatabase
import ru.lopata.madDiary.featureReminders.data.repository.EventRepositoryImpl
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import ru.lopata.madDiary.featureReminders.domain.useCase.event.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {

    @Provides
    @Singleton
    fun providesEventRepository(db: MadNoteDatabase): EventRepository {
        return EventRepositoryImpl(db.eventDao)
    }

    @Provides
    @Singleton
    fun provideEventUseCases(repository: EventRepository): EventUseCases {
        return EventUseCases(
            getEventsUseCase = GetEventsUseCase(repository),
            getEventByIdUseCase = GetEventByIdUseCase(repository),
            createEventUseCase = CreateEventUseCase(repository),
            deleteEventUseCase = DeleteEventUseCase(repository)
        )
    }

}