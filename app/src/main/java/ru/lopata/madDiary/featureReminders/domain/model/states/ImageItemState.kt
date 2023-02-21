package ru.lopata.madDiary.featureReminders.domain.model.states

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItemState(
    val uri: Uri = Uri.EMPTY,
    val size: Long = 0
) : Parcelable