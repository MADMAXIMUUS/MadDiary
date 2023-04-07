package ru.lopata.madDiary.featureReminders.data.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.data.dataSource.EventDao
import ru.lopata.madDiary.featureReminders.domain.model.*
import ru.lopata.madDiary.featureReminders.domain.repository.EventRepository
import java.sql.Date

class EventRepositoryImpl(
    private val eventDao: EventDao
) : EventRepository {

    override fun getEvents(searchQuery: String): Flow<List<EventRepeatNotificationAttachment>> {
        return eventDao.getEvents(searchQuery)
    }

    override suspend fun getEventById(id: Int): EventRepeatNotificationAttachment? {
        return eventDao.getEventById(id)
    }

    override suspend fun getEventsFromDate(startDate: Date): Flow<List<EventRepeatNotificationAttachment>> {
        return eventDao.getEventsFromDate(startDate)
    }

    override suspend fun insertEvent(event: Event): Long {
        return eventDao.insertEvent(event)
    }

    override suspend fun insertRepeat(repeat: Repeat) {
        return eventDao.insertRepeat(repeat)
    }

    override suspend fun insertAttachments(attachments: List<Attachment>) {
        return eventDao.insertAttachments(attachments)
    }

    override suspend fun insertNotifications(notifications: List<Notification>) {
        return eventDao.insertNotifications(notifications)
    }

    override fun getAttachments(type: Int): Flow<List<Attachment>> {
        return eventDao.getAttachments(type)
    }

    override suspend fun getAttachmentsByEventId(eventId: Long): Flow<List<Attachment>> {
        return eventDao.getAttachmentsByEventId(eventId)
    }

    override suspend fun deleteEvent(eventId: Int) {
        return eventDao.deleteEvent(eventId)
    }
}