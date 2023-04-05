package ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates

import androidx.annotation.StringRes

data class DateItem(
    @StringRes val title: Int,
    val date: String
) : DelegateAdapterItem {
    override fun id(): Any {
        return date
    }

    override fun content(): Any {
        return title
    }
}
