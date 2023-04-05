package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class DeleteEventUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(eventId: Int) {
        repository.deleteEvent(eventId)
    }
}