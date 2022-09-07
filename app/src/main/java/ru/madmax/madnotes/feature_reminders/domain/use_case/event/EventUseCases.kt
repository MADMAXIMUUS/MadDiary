package ru.madmax.madnotes.feature_reminders.domain.use_case.event

data class EventUseCases(
    val getEventsUseCase: GetEventsUseCase,
    val getEventByIdUseCase: GetEventByIdUseCase,
    val createEventUseCase: CreateEventUseCase,
    val deleteEventUseCase: DeleteEventUseCase
)