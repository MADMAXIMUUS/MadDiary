package ru.madmax.maddiary.feature_reminders.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.maddiary.feature_reminders.domain.model.Event

interface EventRepository {

    fun getEvents(): Flow<List<Event>>

    suspend fun getEventById(id: Int): Event?

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)
}