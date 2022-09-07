package ru.madmax.madnotes.feature_reminders.data.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.feature_reminders.data.data_source.EventDao
import ru.madmax.madnotes.feature_reminders.domain.model.Event
import ru.madmax.madnotes.feature_reminders.domain.repository.EventRepository

class EventRepositoryImpl(
    private val eventDao: EventDao
) : EventRepository {

    override fun getEvents(): Flow<List<Event>> {
        return eventDao.getEvents()
    }

    override suspend fun getEventById(id: Int): Event? {
        return eventDao.getEventById(id)
    }

    override suspend fun insertEvent(event: Event) {
        return eventDao.insertEvent(event)
    }

    override suspend fun deleteEvent(event: Event) {
        return eventDao.deleteEvent(event)
    }
}