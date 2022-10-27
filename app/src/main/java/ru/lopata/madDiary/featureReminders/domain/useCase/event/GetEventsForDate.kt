package ru.lopata.madDiary.featureReminders.domain.useCase.event

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import java.sql.Date

class GetEventsForDate(val eventRepository: EventRepository) {

    suspend operator fun invoke(date: Date): Flow<List<Event>> {
        return eventRepository.getEventsForDate(date)
    }
}