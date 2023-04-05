package ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates

import android.net.Uri
import androidx.annotation.StringRes
import ru.lopata.madDiary.featureReminders.domain.model.Event

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
) : DelegateAdapterItem {
    override fun id(): Any {
        return id
    }

    override fun content(): Any {
        return title
    }
}
