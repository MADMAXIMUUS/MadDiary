package ru.lopata.madDiary.featureReminders.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository

class GetAttachmentByEventIdUseCase(
    private val repository: EventRepository
) {
    suspend operator fun invoke(eventId: Long): Flow<List<Attachment>> {
        return repository.getAttachmentsByEventId(eventId)
    }
}