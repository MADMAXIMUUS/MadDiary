package ru.madmax.madnotes.feature_reminders.domain.use_case.event

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.feature_reminders.domain.model.Event
import ru.madmax.madnotes.feature_reminders.domain.repository.EventRepository

class GetEventsUseCase(val repository: EventRepository) {

    operator fun invoke(): Flow<List<Event>> {
        return repository.getEvents()
    }
}