package ru.lopata.madDiary.featureReminders.domain.model.states

import android.net.Uri

data class AudioItemState(
    val uri: Uri = Uri.EMPTY,
    val duration: Long = 0,
    val name: String = "",
    val size: Long = 0
)
