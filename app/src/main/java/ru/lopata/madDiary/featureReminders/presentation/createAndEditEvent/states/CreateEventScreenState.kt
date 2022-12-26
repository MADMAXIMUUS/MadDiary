package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states

import android.net.Uri
import androidx.annotation.StringRes
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.EditTextState
import ru.lopata.madDiary.core.util.EventColors
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import java.sql.Date

data class CreateEventScreenState(
    val title: EditTextState = EditTextState(),
    val completed: Boolean = false,
    val startDate: Long = 0,
    val startTime: Long = 0,
    val isStartDateError: Boolean = false,
    val endDate: Long = 0,
    val endTime: Long = 0,
    val isEndDateError: Boolean = false,
    val allDay: Boolean = false,
    val chosenCover: Uri = Uri.EMPTY,
    val color: Int = EventColors.DEFAULT,
    val location: String = "",
    val note: String = "",
    @StringRes
    val repeatTitle: Int = R.string.never,
    val repeat: Long = Repeat.NO_REPEAT,
    val repeatEnd: Date = Date(0),
    @StringRes
    val notificationTitle: List<Int> = listOf(R.string.never),
    val notifications: List<Long> = listOf(-1),
    val covers: List<Uri> = emptyList(),
    val images: List<ImageItemState> = emptyList(),
    val videos: List<VideoItemState> = emptyList(),
    val files: List<FileItemState> = emptyList(),
    val audios: List<FileItemState> = emptyList(),
    val chosenImages: List<ImageItemState> = emptyList(),
    val chosenVideos: List<VideoItemState> = emptyList(),
    val chosenFiles: List<FileItemState> = emptyList(),
    val chosenAudios: List<AudioItemState> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val id: Int? = null
)
