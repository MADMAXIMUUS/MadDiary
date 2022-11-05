package ru.lopata.madDiary.featureReminders.data.dataSource

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventAndRepeat
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import java.sql.Date

@Dao
interface EventDao {

    @Transaction
    @Query("SELECT * FROM EVENTS ORDER BY startDateTime ASC")
    fun getEvents(): Flow<List<EventAndRepeat>>

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE startDateTime > :startDate AND endDateTime < :endDate")
    fun getEventsBetweenDates(startDate: Date, endDate: Date): Flow<List<EventAndRepeat>>

    @Transaction
    @Query("SELECT * FROM EVENTS WHERE startDateTime == :date OR endDateTime==:date")
    fun getEventsForDate(date: Date): Flow<List<EventAndRepeat>>

    @Query("SELECT * FROM EVENTS WHERE eventId = :id")
    suspend fun getEventById(id: Int): EventAndRepeat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepeat(repeat: Repeat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachments(attachments: List<Attachment>)

    @Query("SELECT * FROM ATTACHMENTS WHERE eventOwnerId = :eventId")
    fun getAttachments(eventId: Int): Flow<List<Attachment>>

    @Delete
    suspend fun deleteEvent(event: Event)
}