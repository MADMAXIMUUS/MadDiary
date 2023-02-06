package ru.lopata.madDiary.featureReminders.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.*
import java.sql.Date

interface EventRepository {

    fun getEvents(): Flow<List<EventRepeatNotificationAttachment>>

    suspend fun getEventById(id: Int): EventRepeatNotificationAttachment?

    suspend fun getEventsFromDate(
        startDate: Date
    ): Flow<List<EventRepeatNotificationAttachment>>

    suspend fun insertEvent(event: Event): Long

    suspend fun insertRepeat(repeat: Repeat)

    suspend fun insertAttachments(attachments: List<Attachment>)

    suspend fun insertNotifications(notifications: List<Notification>)

    fun getAttachments(type: Int): Flow<List<Attachment>>

    suspend fun getAttachmentsByEventId(eventId: Long): Flow<List<Attachment>>

    suspend fun deleteEvent(event: Event)
}