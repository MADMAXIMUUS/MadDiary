package ru.lopata.madDiary.featureReminders.data.dataSource

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.*
import java.sql.Date

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE title LIKE '%' || :searchQuery || '%' ORDER BY startDateTime ASC")
    fun getEvents(searchQuery: String): Flow<List<EventRepeatNotificationAttachment>>

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE startDateTime >= :startDate")
    fun getEventsFromDate(startDate: Date): Flow<List<EventRepeatNotificationAttachment>>

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE eventId = :id")
    suspend fun getEventById(id: Int): EventRepeatNotificationAttachment?

    @Query("SELECT * FROM ATTACHMENTS WHERE type = :type")
    fun getAttachments(type: Int = Attachment.FILE): Flow<List<Attachment>>

    @Query("SELECT * FROM ATTACHMENTS WHERE eventOwnerId = :eventId")
    fun getAttachmentsByEventId(eventId: Long): Flow<List<Attachment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepeat(repeat: Repeat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<Notification>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<Attachment>)

    @Query("DELETE FROM EVENTS WHERE eventId = :eventId")
    suspend fun deleteEvent(eventId: Int)
}