package ru.lopata.madDiary.featureReminders.domain.useCase.event

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatAttachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import java.sql.Date

class GetEventsBetweenDatesUseCase(val repository: EventRepository) {

    suspend operator fun invoke(startDate: Date, endDate: Date): Flow<List<EventRepeatAttachment>> {
        return repository.getEventsBetweenDates(startDate, endDate)
    }
}