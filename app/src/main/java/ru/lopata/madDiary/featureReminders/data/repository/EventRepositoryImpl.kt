package ru.lopata.madDiary.featureReminders.data.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.data.dataSource.EventDao
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import java.sql.Date

class EventRepositoryImpl(
    private val eventDao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return eventDao.getEvents()
    }

    override suspend fun getEventById(id: Int): Event? {
        return eventDao.getEventById(id)
    }

    override suspend fun getEventsBetweenDates(startDate: Date, endDate: Date): Flow<List<Event>> {
        return eventDao.getEventsBetweenDates(startDate, endDate)
    }

    override suspend fun getEventsForDate(date: Date): Flow<List<Event>> {
        return eventDao.getEventsForDate(date)
    }

    override suspend fun insertEvent(event: Event) {
        return eventDao.insertEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        return eventDao.deleteEvent(event)
    }
}