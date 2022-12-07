package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.net.Uri
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState

interface OnAttachmentChosenListener {

    fun onCoverChosen(uri: Uri)
    fun onImagesChosen(items: List<Uri>)
    fun onVideosChosen(items: List<VideoItemState>)
    fun onAudioChosen(uri: Uri)
    fun onFileChosen(uri: Uri)
}