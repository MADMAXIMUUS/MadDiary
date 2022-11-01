package ru.lopata.madDiary.featureReminders.domain.model

import androidx.annotation.StringRes

sealed class MainScreenItem {
    data class TitleItem(
        @StringRes val title: Int,
        val date: String
    ) : MainScreenItem()

    data class EventItem(
        val id: Int,
        val title: String,
        val type: Int,
        val startTime: String,
        val endTime: String,
        val address: String,
        val color: Int,
        val isNotificationSet: Boolean,
        val cover: String,
        val isChecked: Boolean
    ) : MainScreenItem()

    companion object {
        const val TITLE = 0
        const val EVENT = 1
        const val TASK = 2
        const val REMINDER = 3
    }
}