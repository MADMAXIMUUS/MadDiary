package ru.lopata.madDiary.featureReminders.domain.model

import android.net.Uri
import androidx.annotation.StringRes

sealed class MainScreenItem {
    data class MonthYearTitleItem(
        val month: String,
        val yearNumber: Int
    ) : MainScreenItem()

    data class DateItem(
        @StringRes val title: Int,
        val date: String
    ) : MainScreenItem()

    data class EventItem(
        val id: Int,
        val chapter: Int,
        val chapters: Int,
        val title: String,
        val pass: Boolean,
        val type: Event.Types,
        @StringRes val subtitleFrom: Int,
        val startTime: String,
        @StringRes val subtitleTo: Int,
        val endTime: String,
        val address: String,
        val color: Int,
        val isNotificationSet: Boolean,
        val isAttachmentAdded: Boolean,
        val cover: Uri,
        val isChecked: Boolean
    ) : MainScreenItem()

    companion object {
        const val BIG_TITLE = 0
        const val TITLE = 1
        const val EVENT = 2
    }
}
