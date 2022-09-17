package ru.madmax.madDiary.featureReminders.domain.useCase.event

data class EventUseCases(
    val getEventsUseCase: GetEventsUseCase,
    val getEventByIdUseCase: GetEventByIdUseCase,
    val createEventUseCase: CreateEventUseCase,
    val deleteEventUseCase: DeleteEventUseCase
)