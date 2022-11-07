package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatAttachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetEventByIdUseCase(val repository: EventRepository) {

    suspend operator fun invoke(id: Int): EventRepeatAttachment? {
        return repository.getEventById(id)
    }
}