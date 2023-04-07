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
        return EventItemContent(isChecked, pass)
    }

    override fun payload(other: Any): DelegateAdapterItem.Payloadable {
        if (other is EventItem) {
            if (isChecked != other.isChecked)
                return ChangePayload.CompletedChange(other.isChecked, other.pass)
        }
        return DelegateAdapterItem.Payloadable.None
    }

    inner class EventItemContent(
        val isCompleted: Boolean,
        val pass: Boolean
    ) {
        override fun equals(other: Any?): Boolean {
            if (other is EventItemContent) {
                return pass == other.pass && isCompleted == other.isCompleted
            }
            return false
        }

        override fun hashCode(): Int {
            var result = pass.hashCode()
            result = 31 * result + isCompleted.hashCode()
            return result
        }
    }

    sealed class ChangePayload : DelegateAdapterItem.Payloadable {
        data class CompletedChange(val isCompleted: Boolean, val pass: Boolean) : ChangePayload()
    }

}
