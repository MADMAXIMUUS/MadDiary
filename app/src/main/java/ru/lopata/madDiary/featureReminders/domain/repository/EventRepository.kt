package ru.lopata.madDiary.featureReminders.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Event
import java.sql.Date

interface EventRepository {

    fun getEvents(): Flow<List<Event>>

    suspend fun getEventById(id: Int): Event?

    suspend fun getEventsBetweenDates(startDate: Date, endDate: Date): Flow<List<Event>>

    suspend fun getEventsForDate(date: Date): Flow<List<Event>>

    suspend fun insertEvent(event: Event)

    suspend fun deleteEvent(event: Event)
}