package ru.lopata.madDiary.featureReminders.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "ATTACHMENTS",
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            onDelete = CASCADE,
            childColumns = ["eventOwnerId"]
        )]
)
data class Attachment(
    @PrimaryKey val atId: Int? = null,
    val eventOwnerId: Int = -1,
    val type: Int = IMAGE,
    val uri: String = "empty"
): Parcelable {
    companion object {
        const val IMAGE = 0
        const val AUDIO = 1
        const val FILE = 2
        const val VIDEO = 3
        const val URL = 4
    }
}
