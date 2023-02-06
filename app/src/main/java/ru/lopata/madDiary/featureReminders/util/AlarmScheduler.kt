package ru.lopata.madDiary.featureReminders.util

import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatNotificationAttachment

interface AlarmScheduler {
    fun schedule(item: EventRepeatNotificationAttachment)
    fun cancel(item: EventRepeatNotificationAttachment)
}