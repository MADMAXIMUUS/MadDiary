package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states

import android.net.Uri

data class VideoItemState(
    val uri: Uri = Uri.EMPTY,
    val duration: Long = 0,
    val size: Long = 0
)
