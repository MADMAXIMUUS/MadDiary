package ru.madmax.madnotes.feature_reminders.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.madmax.madnotes.feature_reminders.domain.model.Event

@Dao
interface EventDao {

    @Query("SELECT * FROM EVENTS")
    fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM EVENTS WHERE eventId = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}