package ru.lopata.madDiary.featureReminders.domain.useCase

import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class UpdateEventUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(event: Event){
        repository.updateEvent(event)
    }
}