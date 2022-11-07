package ru.lopata.madDiary.featureReminders.domain.useCase.event

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatAttachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import java.sql.Date

class GetEventsForDate(val eventRepository: EventRepository) {

    suspend operator fun invoke(date: Date): Flow<List<EventRepeatAttachment>> {
        return eventRepository.getEventsForDate(date)
    }
}