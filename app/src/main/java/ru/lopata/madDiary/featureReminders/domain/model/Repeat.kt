package ru.lopata.madDiary.featureReminders.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.sql.Date

@Entity(
    tableName = "REPEATS",
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            onDelete = CASCADE,
            childColumns = ["eventOwnerId"]
        )
    ]
)
@Parcelize
data class Repeat(
    val repeatStart: Date = Date(0),
    val repeatInterval: Long = NO_REPEAT,
    val repeatEnd: Date = Date(0),
    val eventOwnerId: Int = -1,

    @PrimaryKey val repeatId: Int? = null
):Parcelable {
    companion object {
        const val NO_REPEAT: Long = 0
        const val EVERY_DAY: Long = 86400000
        const val EVERY_SECOND_DAY: Long = EVERY_DAY * 2
        const val EVERY_WEEK: Long = EVERY_DAY * 7
        const val EVERY_SECOND_WEEK: Long = EVERY_WEEK * 2
        const val EVERY_MONTH: Long = EVERY_DAY * 30
        const val EVERY_YEAR: Long = EVERY_DAY * 365
    }
}
