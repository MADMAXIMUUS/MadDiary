package ru.lopata.madDiary.featureReminders.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatAttachment
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import java.sql.Date

interface EventRepository {

    fun getEvents(): Flow<List<EventRepeatAttachment>>

    suspend fun getEventById(id: Int): EventRepeatAttachment?

    suspend fun getEventsBetweenDates(startDate: Date, endDate: Date): Flow<List<EventRepeatAttachment>>

    suspend fun getEventsForDate(date: Date): Flow<List<EventRepeatAttachment>>

    suspend fun insertEvent(event: Event) : Long

    suspend fun insertRepeat(repeat: Repeat)

    suspend fun deleteEvent(event: Event)
}