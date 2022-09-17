package ru.madmax.madDiary.featureReminders.domain.useCase.event

import ru.madmax.madDiary.featureReminders.domain.model.Event
import ru.madmax.madDiary.featureReminders.domain.repository.EventRepository

class GetEventByIdUseCase(val repository: EventRepository) {

    suspend operator fun invoke(id: Int): Event?{
        return repository.getEventById(id)
    }
}