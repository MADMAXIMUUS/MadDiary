package ru.lopata.madDiary.featureReminders.data.dataSource

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.lopata.madDiary.featureReminders.domain.model.Event
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

@Dao
interface EventDao {

    @Query("SELECT * FROM EVENTS ORDER BY startDateTime ASC")
    fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM EVENTS WHERE startDateTime > :startDate AND endDateTime < :endDate")
    fun getEventsBetweenDates(startDate: Calendar, endDate: Calendar): Flow<List<Event>>

    @Query("SELECT * FROM EVENTS WHERE startDateTime == :date or endDateTime==:date")
    fun getEventsForDate(date: Calendar): Flow<List<Event>>

    @Query("SELECT * FROM EVENTS WHERE eventId = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}