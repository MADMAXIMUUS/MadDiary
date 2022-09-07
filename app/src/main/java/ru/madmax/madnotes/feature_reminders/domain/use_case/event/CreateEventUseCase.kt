package ru.madmax.madnotes.feature_reminders.domain.use_case.event

import ru.madmax.madnotes.feature_reminders.domain.model.Event
import ru.madmax.madnotes.feature_reminders.domain.repository.EventRepository

class CreateEventUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        repository.insertEvent(event)
    }
}