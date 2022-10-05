package ru.lopata.madDiary.featureReminders.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.madDiary.featureReminders.domain.model.Event

interface EventRepository {

    fun getEvents(): Flow<List<Event>>

    suspend fun getEventById(id: Int): Event?

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)
}