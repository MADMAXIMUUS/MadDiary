package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class CreateEventUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(event: Event): Long {
        return repository.insertEvent(event)
    }
}