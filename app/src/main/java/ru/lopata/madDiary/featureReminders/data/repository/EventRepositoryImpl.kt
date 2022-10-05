package ru.lopata.madDiary.featureReminders.data.repository

import kotlinx.coroutines.flow.Flow
import ru.madmax.madDiary.featureReminders.data.dataSource.EventDao
import ru.madmax.madDiary.featureReminders.domain.model.Event
import ru.madmax.madDiary.featureReminders.domain.repository.EventRepository

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