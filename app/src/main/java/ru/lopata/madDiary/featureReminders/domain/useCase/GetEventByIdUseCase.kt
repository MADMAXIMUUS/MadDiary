package ru.lopata.madDiary.featureReminders.domain.useCase

import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetEventByIdUseCase(val repository: EventRepository) {

    suspend operator fun invoke(id: Int): EventRepeatNotificationAttachment? {
        return repository.getEventById(id)
    }
}