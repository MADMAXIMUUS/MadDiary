package ru.lopata.madDiary.featureReminders.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.model.Notification
import ru.lopata.madDiary.featureReminders.domain.receivers.AlarmReceiver

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("ShortAlarm")
    override fun schedule(item: EventRepeatNotificationAttachment, chanelID: String) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("event", item)
            putExtra("chanelId", chanelID)
        }

        if (item.notifications.isNotEmpty() && item.notifications[0].time != Notification.NEVER) {
            item.notifications.forEach {
                val time = item.event.startDateTime.time - it.time
                val id = item.event.eventId?.plus(it.time.toInt()) ?: item.hashCode()

                if (PendingIntent.getBroadcast(
                        context,
                        id,
                        intent,
                        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
                    ) == null
                ) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC,
                        time,
                        PendingIntent.getBroadcast(
                            context,
                            id,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                }
            }
        }
    }

    override fun cancel(item: EventRepeatNotificationAttachment) {
        if (item.notifications.isNotEmpty() && item.notifications[0].time != Notification.NEVER) {
            item.notifications.forEach {
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    item.event.eventId?.plus(it.time.toInt()) ?: item.hashCode(),
                    Intent(context, AlarmReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
    }
}