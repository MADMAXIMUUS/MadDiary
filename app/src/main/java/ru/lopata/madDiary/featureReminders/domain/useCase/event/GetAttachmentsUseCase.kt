package ru.lopata.madDiary.featureReminders.domain.useCase.event

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetAttachmentsUseCase(
    private val repository: EventRepository
) {
    operator fun invoke(type: Int = Attachment.FILE): Flow<List<Attachment>> {
        return repository.getAttachments(type)
    }
}