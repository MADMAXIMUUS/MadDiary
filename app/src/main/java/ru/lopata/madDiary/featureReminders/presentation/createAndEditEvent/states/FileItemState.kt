package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FileItemState(
    val uri: Uri = Uri.EMPTY,
    val size: Long = 0,
    val sizeTitle: String = "",
    val name: String = "",
    val type: String = ""
) : Parcelable