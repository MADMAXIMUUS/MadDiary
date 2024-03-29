package ru.lopata.madDiary.featureReminders.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
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
    val duration: Long = 0,
    val size: Long = 0,
    val name: String = "",
    val fileExtension: String = "",
    val uri: String = ""
) : Parcelable {
    companion object {
        const val IMAGE = 0
        const val VIDEO = 1
        const val AUDIO = 2
        const val FILE = 3
    }
}
