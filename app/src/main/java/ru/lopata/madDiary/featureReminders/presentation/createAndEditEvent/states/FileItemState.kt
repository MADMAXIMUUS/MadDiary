package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states

import android.net.Uri

data class FileItemState(
    val uri: Uri = Uri.EMPTY,
    val size: Long = 0,
    val sizeTitle: String = "",
    val name: String = "",
    val type: String = ""
)