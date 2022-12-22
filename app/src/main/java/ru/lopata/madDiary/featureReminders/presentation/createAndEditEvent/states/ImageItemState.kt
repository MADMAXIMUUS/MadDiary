package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states

import android.net.Uri

data class ImageItemState(
    val uri: Uri = Uri.EMPTY,
    val size: Long = 0
)