package ru.lopata.madDiary.featureReminders.domain.useCase.event

import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class CreateAttachmentsUseCase(
    val repository: EventRepository
) {
    suspend operator fun invoke(attachments: List<Attachment>) {
        return repository.insertAttachments(attachments)
    }
}