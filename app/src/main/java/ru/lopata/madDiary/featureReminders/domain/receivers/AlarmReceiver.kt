package ru.lopata.madDiary.featureReminders.domain.receivers

import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.presentation.MainActivity
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.util.AndroidAlarmScheduler
import java.sql.Date

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val item = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("event", EventRepeatNotificationAttachment::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("event") as? EventRepeatNotificationAttachment
        }

        val chanelId = intent.getStringExtra("chanelId").toString()

        val tapResultIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("eventId", item?.event?.eventId)
            putExtra("eventType", item?.event?.type.toString())
        }

        val pendingIntent: PendingIntent =
            getActivity(context, 0, tapResultIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notificationBuilder =
            NotificationCompat.Builder(context, chanelId)
                .setContentTitle(item?.event?.title)
                .setContentText(item?.event?.note)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .setBigContentTitle(item?.event?.title)
                        .bigText(item?.event?.note)
                ).setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)

        try {
            notificationManager.notify(item?.event?.eventId ?: 1, notificationBuilder.build())

        } catch (e: SecurityException) {
            e.localizedMessage?.let { it1 -> Log.e("SecurityException", it1) }
        }

        if (item != null) {
            if (item.repeat != null) {
                val alarmScheduler = AndroidAlarmScheduler(context)
                alarmScheduler.cancel(item)
                if (item.event.startDateTime.time + item.repeat.repeatInterval <= item.repeat.repeatEnd.time)
                    alarmScheduler.schedule(
                        item.copy(
                            event = item.event.copy(
                                startDateTime = Date(item.event.startDateTime.time + item.repeat.repeatInterval)
                            )
                        ),
                        chanelID = chanelId
                    )
            }
        }
    }
}