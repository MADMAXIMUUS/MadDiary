package ru.lopata.madDiary.featureReminders.domain.model

import java.util.*

sealed class MainScreenItem {
    data class TitleItem(
        val title: String,
        val date: Date
    ) : MainScreenItem()

    data class EventItem(
        val id: Int,
        val title: String,
        val type: Int,
        val time: Long,
        val address: String,
        val color: Int,
        val isNotificationSet: Boolean,
        val isAttachmentAdded: Boolean,
        val isChecked: Boolean
    ) : MainScreenItem()

    companion object{
        const val TITLE = 0
        const val EVENT = 1
        const val TASK = 2
        const val REMINDER = 3
    }
}
