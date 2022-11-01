package ru.lopata.madDiary.featureReminders.domain.useCase.event

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventAndRepeat
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetEventsUseCase(val repository: EventRepository) {

    operator fun invoke(): Flow<List<EventAndRepeat>> {
        return repository.getEvents()
    }
}