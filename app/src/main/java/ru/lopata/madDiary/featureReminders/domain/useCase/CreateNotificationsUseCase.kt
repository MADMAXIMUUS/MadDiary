package ru.lopata.madDiary.featureReminders.domain.useCase

import ru.lopata.madDiary.featureReminders.domain.model.Notification
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class CreateNotificationsUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(attachments: List<Notification>) {
        return repository.insertNotifications(attachments)
    }
}