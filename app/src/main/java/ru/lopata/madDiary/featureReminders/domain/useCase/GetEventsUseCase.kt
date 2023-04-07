package ru.lopata.madDiary.featureReminders.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetEventsUseCase(val repository: EventRepository) {

    operator fun invoke(searchQuery: String=""): Flow<List<EventRepeatNotificationAttachment>> {
        return repository.getEvents(searchQuery)
    }
}