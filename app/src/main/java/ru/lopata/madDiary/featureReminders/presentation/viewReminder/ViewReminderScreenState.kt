package ru.lopata.madDiary.featureReminders.presentation.viewReminder

import android.net.Uri
import androidx.annotation.StringRes
import ru.lopata.madDiary.R
import ru.lopata.madDiary.featureReminders.domain.model.*
import java.sql.Date

data class ViewReminderScreenState(
    val title: String = "",
    val completed: Boolean = false,
    val cover: Uri = Uri.EMPTY,
    val startDateTime: Date = Date(0),
    val chapter: Int = 1,
    val chapters: Int = 1,
    val allDay: Boolean = false,
    val color: Int = -1,
    val location: String = "",
    val notificationTitle: List<Int> = listOf(R.string.never),
    val notifications: List<Long> = listOf(Notification.NEVER),
    @StringRes
    val repeatTitle: Int = R.string.never,
    val repeat: Long = Repeat.NO_REPEAT,
    val repeatEnd: Date = Date(0),
    val attachments: List<Attachment> = emptyList(),
    val eventId: Int = -1
) {
    fun toEvent(): Event {
        return Event(
            eventId = eventId,
            title = title,
            completed = completed,
            startDateTime = startDateTime,
            endDateTime = startDateTime,
            allDay = allDay,
            color = color,
            location = location,
            note = "",
            isAttachmentAdded = attachments.isNotEmpty()
        )
    }

    fun toEventRepeatNotificationAttachment(): EventRepeatNotificationAttachment {
        val event = Event(
            eventId = eventId,
            title = title,
            completed = completed,
            startDateTime = startDateTime,
            endDateTime = startDateTime,
            allDay = allDay,
            color = color,
            location = location,
            note = "",
            isAttachmentAdded = attachments.isNotEmpty()
        )
        val repeat = Repeat(
            repeatStart = startDateTime,
            repeatInterval = repeat,
            repeatEnd = repeatEnd
        )
        val list = mutableListOf<Notification>()
        notifications.forEach {
            list.add(
                Notification(
                    time = it
                )
            )
        }
        return EventRepeatNotificationAttachment(
            event, repeat, attachments, list
        )
    }
}