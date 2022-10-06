package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetEventByIdUseCase(val repository: EventRepository) {

    suspend operator fun invoke(id: Int): Event?{
        return repository.getEventById(id)
    }
}