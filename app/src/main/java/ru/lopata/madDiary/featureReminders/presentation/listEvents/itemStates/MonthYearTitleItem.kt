package ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates

import android.net.Uri

data class MonthYearTitleItem(
    val month: String,
    val yearNumber: Int,
    val image: Uri
) : DelegateAdapterItem {

    override fun id(): Any {
        return month + yearNumber
    }

    override fun content(): Any {
        return image
    }
}