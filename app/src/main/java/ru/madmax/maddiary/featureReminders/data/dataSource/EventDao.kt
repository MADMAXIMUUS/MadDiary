package ru.madmax.madDiary.featureReminders.data.dataSource

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.madmax.madDiary.featureReminders.domain.model.Event
import java.sql.Timestamp

@Dao
interface EventDao {

    @Query("SELECT * FROM EVENTS ORDER BY startTimestamp ASC")
    fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM EVENTS WHERE startTimestamp > :startDate AND endTimestamp < :endDate")
    fun getEventsBetweenDates(startDate: Timestamp, endDate: Timestamp): Flow<List<Event>>

    @Query("SELECT * FROM EVENTS WHERE eventId = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}