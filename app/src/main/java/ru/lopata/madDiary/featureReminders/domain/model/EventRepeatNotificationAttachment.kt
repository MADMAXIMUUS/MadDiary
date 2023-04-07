package ru.lopata.madDiary.featureReminders.domain.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.toTimeZone
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.EventItem

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
) : Parcelable{

    fun toEventItem() = EventItem(
        id = event.eventId ?: -1,
        chapter = 0,
        chapters = 0,
        title = event.title,
        pass = false,
        type = event.type,
        subtitleFrom = when (event.type) {
            Event.Types.EVENT -> {
                R.string.from
            }
            Event.Types.TASK -> {
                R.string.reminder_task_deadline
            }
            Event.Types.REMINDER -> {
                R.string.reminder_reminder_date
            }
        },
        startTime = event.startDateTime.time.toTimeZone(),
        subtitleTo = when (event.type) {
            Event.Types.EVENT -> {
                R.string.to
            }
            Event.Types.TASK, Event.Types.REMINDER -> {
                -1
            }
        },
        endTime = event.endDateTime.time.toTimeZone(),
        address = event.location,
        color = event.color,
        isNotificationSet = notifications.isNotEmpty(),
        isAttachmentAdded = attachments.isNotEmpty(),
        cover = Uri.parse(event.cover),
        isChecked = event.completed
    )
}
