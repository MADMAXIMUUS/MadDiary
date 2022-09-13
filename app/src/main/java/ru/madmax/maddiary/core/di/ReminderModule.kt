package ru.madmax.maddiary.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.madmax.maddiary.core.data.data_source.MadNoteDatabase
import ru.madmax.maddiary.feature_reminders.data.repository.EventRepositoryImpl
import ru.madmax.maddiary.feature_reminders.domain.repository.EventRepository
import ru.madmax.maddiary.feature_reminders.domain.use_case.event.*
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