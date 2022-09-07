package ru.madmax.madnotes.feature_reminders.domain.use_case.event

import ru.madmax.madnotes.feature_reminders.domain.model.Event
import ru.madmax.madnotes.feature_reminders.domain.repository.EventRepository

class GetEventByIdUseCase(val repository: EventRepository) {

    suspend operator fun invoke(id: Int): Event?{
        return repository.getEventById(id)
    }
}