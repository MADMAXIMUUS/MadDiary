package ru.lopata.madDiary.featureReminders.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventAndRepeat
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import java.sql.Date

interface EventRepository {

    fun getEvents(): Flow<List<EventAndRepeat>>

    suspend fun getEventById(id: Int): Event?

    suspend fun getEventsBetweenDates(startDate: Date, endDate: Date): Flow<List<EventAndRepeat>>

    suspend fun getEventsForDate(date: Date): Flow<List<EventAndRepeat>>

    suspend fun insertEvent(event: Event) : Long

    suspend fun insertRepeat(repeat: Repeat)

    suspend fun deleteEvent(event: Event)
}