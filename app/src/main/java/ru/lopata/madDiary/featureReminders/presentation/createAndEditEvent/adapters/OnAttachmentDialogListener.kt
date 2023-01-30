package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.net.Uri
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.AudioItemState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.FileItemState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.ImageItemState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState

interface OnAttachmentDialogListener {
    fun onPermissionButtonClick()
    fun onInternalButtonClick()
    fun onCoverChosen(uri: Uri)
    fun onImagesChosen(items: List<ImageItemState>)
    fun onImageChosen(item: ImageItemState, state: Boolean)
    fun onVideosChosen(items: List<VideoItemState>)
    fun onVideoChosen(item: VideoItemState, state: Boolean)
    fun onAudioChosen(item: AudioItemState)
    fun onFileChosen(item: FileItemState)
    fun onImageDialogShow(item: ImageItemState, isChosen: Boolean)
    fun onVideoDialogShow(item: VideoItemState, isChosen: Boolean)
    fun onAudioDialogShow(item: AudioItemState, isChosen: Boolean)
}