package ru.lopata.madDiary.featureReminders.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventRepeatNotificationAttachment(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventOwnerId"
    )
    val repeat: Repeat?,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventOwnerId"
    )
    val attachments: List<Attachment>,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventOwnerId"
    )
    val notifications: List<Notification>
) : Parcelable
