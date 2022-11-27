package ru.lopata.madDiary.featureReminders.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import ru.lopata.madDiary.core.util.EventColors
import java.sql.Date

@Parcelize
@Entity(tableName = "EVENTS")
data class Event(
    val title: String = "",
    val completed: Boolean = false,
    val startDateTime: Date = Date(0),
    val endDateTime: Date = Date(0),
    val allDay: Boolean = false,
    val color: Int = EventColors.DEFAULT,
    val cover: String = "empty",
    val location: String = "",
    val note: String = "",
    val isAttachmentAdded: Boolean = false,

    @PrimaryKey(autoGenerate = true) val eventId: Int? = null
) : Parcelable