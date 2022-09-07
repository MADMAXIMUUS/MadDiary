package ru.madmax.madnotes.feature_reminders.domain.use_case.event

import ru.madmax.madnotes.feature_reminders.domain.model.Event
import ru.madmax.madnotes.feature_reminders.domain.repository.EventRepository

class DeleteEventUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        repository.deleteEvent(event)
    }
}