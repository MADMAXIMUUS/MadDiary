package ru.lopata.madDiary.featureReminders.domain.useCase.event

import kotlinx.coroutines.flow.Flow
import ru.madmax.madDiary.featureReminders.domain.model.Event
import ru.madmax.madDiary.featureReminders.domain.repository.EventRepository

class GetEventsUseCase(val repository: EventRepository) {

    operator fun invoke(): Flow<List<Event>> {
        return repository.getEvents()
    }
}