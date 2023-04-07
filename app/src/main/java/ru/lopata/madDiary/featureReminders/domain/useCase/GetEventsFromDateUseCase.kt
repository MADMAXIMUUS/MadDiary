package ru.lopata.madDiary.featureReminders.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import java.sql.Date

class GetEventsFromDateUseCase(val repository: EventRepository) {

    suspend operator fun invoke(startDate: Date): Flow<List<EventRepeatNotificationAttachment>> {
        return repository.getEventsFromDate(startDate)
    }
}