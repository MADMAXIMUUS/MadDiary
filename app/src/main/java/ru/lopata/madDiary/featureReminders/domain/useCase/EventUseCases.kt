package ru.lopata.madDiary.featureReminders.domain.useCase

data class EventUseCases(
    val getEventsUseCase: GetEventsUseCase,
    val getEventByIdUseCase: GetEventByIdUseCase,
    val getEventsFromDateUseCase: GetEventsFromDateUseCase,
    val createEventUseCase: CreateEventUseCase,
    val createRepeatUseCase: CreateRepeatUseCase,
    val createAttachmentsUseCase: CreateAttachmentsUseCase,
    val createNotificationsUseCase: CreateNotificationsUseCase,
    val getAttachmentByEventIdUseCase: GetAttachmentByEventIdUseCase,
    val getAttachmentsUseCase: GetAttachmentsUseCase,
    val deleteEventUseCase: DeleteEventUseCase
)