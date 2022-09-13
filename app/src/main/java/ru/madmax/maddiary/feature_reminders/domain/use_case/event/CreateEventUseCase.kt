package ru.madmax.maddiary.feature_reminders.domain.use_case.event

import ru.madmax.maddiary.feature_reminders.domain.model.Event
import ru.madmax.maddiary.feature_reminders.domain.repository.EventRepository

class CreateEventUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        repository.insertEvent(event)
    }
}