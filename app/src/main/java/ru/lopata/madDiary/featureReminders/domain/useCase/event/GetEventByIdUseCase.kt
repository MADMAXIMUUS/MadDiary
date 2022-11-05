package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.model.EventAndRepeat
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetEventByIdUseCase(val repository: EventRepository) {

    suspend operator fun invoke(id: Int): EventAndRepeat? {
        return repository.getEventById(id)
    }
}