package ru.lopata.madDiary.featureReminders.domain.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment
import ru.lopata.madDiary.featureReminders.domain.model.Notification
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import ru.lopata.madDiary.featureReminders.util.AndroidAlarmScheduler
import java.sql.Date
import javax.inject.Inject

@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var eventUseCases: EventUseCases

    override fun onReceive(context: Context, intent: Intent) {
        val calendar = Calendar.getInstance()
        val date = Date(calendar.timeInMillis)
        CoroutineScope(IO).launch {
            eventUseCases.getEventsFromDateUseCase(date).collectLatest { items ->
                items.forEach { item ->
                    if (item.notifications.isNotEmpty() && item.notifications[0].time != Notification.NEVER) {
                        setAlarm(item, context)
                    }
                }
            }
        }
    }

    private fun setAlarm(item: EventRepeatNotificationAttachment, context: Context) {
        val alarmScheduler = AndroidAlarmScheduler(context)
        alarmScheduler.schedule(item)
    }
}