package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent

import android.content.ContentResolver
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.lopata.madDiary.BuildConfig
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.EditTextState
import ru.lopata.madDiary.core.util.UiEvent
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.EventRepeatAttachment
import ru.lopata.madDiary.featureReminders.domain.model.Repeat
import ru.lopata.madDiary.featureReminders.domain.useCase.event.EventUseCases
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.CreateEventScreenState
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState
import java.sql.Date
import javax.inject.Inject


@HiltViewModel
class CreateAndEditEventViewModel @Inject constructor(
    private val eventUseCases: EventUseCases,
    state: SavedStateHandle
) : ViewModel() {

    private val _currentEvent = MutableStateFlow(CreateEventScreenState())
    val currentEvent = _currentEvent.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var preEditEvent: Event = Event()
    private var preEditRepeat: Repeat = Repeat()

    init {
        val calendarStart = Calendar.getInstance()
        calendarStart.timeZone = TimeZone.getDefault()
        val calendarEnd = Calendar.getInstance()
        calendarEnd.timeZone = TimeZone.getDefault()
        calendarEnd.timeInMillis = calendarStart.timeInMillis
        calendarEnd.add(Calendar.HOUR_OF_DAY, 1)
        val hourStart = calendarStart.get(Calendar.HOUR_OF_DAY)
        val minuteStart = calendarStart.get(Calendar.MINUTE)
        val startTime = hourStart * 60L * 60L * 1000L + minuteStart * 60L * 1000L
        val hourEnd = calendarEnd.get(Calendar.HOUR_OF_DAY)
        val minuteEnd = calendarEnd.get(Calendar.MINUTE)
        val endTime = hourEnd * 60L * 60L * 1000L + minuteEnd * 60L * 1000L
        val covers = listOf(
            getURLForResource(R.drawable.cover_1),
            getURLForResource(R.drawable.cover_2),
            getURLForResource(R.drawable.cover_3),
            getURLForResource(R.drawable.cover_4),
            getURLForResource(R.drawable.cover_5),
            getURLForResource(R.drawable.cover_6),
            getURLForResource(R.drawable.cover_7),
            getURLForResource(R.drawable.cover_9),
            getURLForResource(R.drawable.cover_11)
        )
        _currentEvent.update { currentValue ->
            currentValue.copy(
                startDate = calendarStart.apply {
                    set(Calendar.HOUR, 0)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.MILLISECONDS_IN_DAY, 0)
                }.timeInMillis,
                startTime = startTime,
                endDate = calendarEnd.apply {
                    set(Calendar.HOUR, 0)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.MILLISECONDS_IN_DAY, 0)
                }.timeInMillis,
                endTime = endTime,
                covers = covers
            )
        }
        state.get<EventRepeatAttachment>("eventRepeatAttachments")?.let { eventRepeatAttachments ->
            viewModelScope.launch {
                val event = eventRepeatAttachments.event
                preEditEvent = event
                val repeat = eventRepeatAttachments.repeat ?: Repeat()
                val attachments = eventRepeatAttachments.attachments
                preEditRepeat = repeat
                calendarStart.timeInMillis = event.startDateTime.time
                calendarStart.apply {
                    set(Calendar.HOUR, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.MILLISECONDS_IN_DAY, 0)
                }
                calendarEnd.timeInMillis = event.endDateTime.time
                calendarEnd.apply {
                    set(Calendar.HOUR, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.MILLISECONDS_IN_DAY, 0)
                }
                val images = mutableListOf<Uri>()
                val videos = mutableListOf<VideoItemState>()
                attachments.forEach {
                    when (it.type) {
                        Attachment.IMAGE -> {
                            images.add(Uri.parse(it.uri))
                        }
                        Attachment.VIDEO -> {
                            videos.add(
                                VideoItemState(
                                    uri = Uri.parse(it.uri),
                                    size = it.size,
                                    duration = it.duration
                                )
                            )
                        }
                    }
                }
                _currentEvent.update { currentValue ->
                    currentValue.copy(
                        title = EditTextState(
                            text = event.title,
                            isEmpty = false,
                            isError = false
                        ),
                        completed = event.completed,
                        startDate = calendarStart.timeInMillis,
                        startTime = event.startDateTime.time - calendarStart.timeInMillis,
                        endDate = calendarEnd.timeInMillis,
                        endTime = event.endDateTime.time - calendarEnd.timeInMillis,
                        allDay = event.allDay,
                        color = event.color,
                        location = event.location,
                        note = event.note,
                        repeat = repeat.repeatInterval,
                        repeatEnd = repeat.repeatEnd,
                        id = event.eventId,
                        attachments = attachments,
                        chosenCover = Uri.parse(event.cover),
                        chosenImages = images,
                        chosenVideos = videos
                    )
                }
                _eventFlow.emit(UiEvent.UpdateUiState)
                when (repeat.repeatInterval) {
                    Repeat.NO_REPEAT -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.never
                            )
                        }
                    }
                    Repeat.EVERY_DAY -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_day
                            )
                        }
                    }
                    Repeat.EVERY_SECOND_DAY -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_second_day
                            )
                        }
                    }
                    Repeat.EVERY_WEEK -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_week
                            )
                        }
                    }
                    Repeat.EVERY_SECOND_WEEK -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_second_week
                            )
                        }
                    }
                    Repeat.EVERY_MONTH -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_month
                            )
                        }
                    }
                    Repeat.EVERY_YEAR -> {
                        _currentEvent.update { currentValue ->
                            currentValue.copy(
                                repeatTitle = R.string.every_year
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateColor(value: Int) {
        _currentEvent.update { currentValue ->
            currentValue.copy(
                color = value
            )
        }
    }

    fun updateStartDate(value: Long) {
        _currentEvent.value = currentEvent.value.copy(
            startDate = value,
            isStartDateError = value + currentEvent.value.startTime >=
                    currentEvent.value.endDate + currentEvent.value.endTime,
            isEndDateError = value + currentEvent.value.startTime >=
                    currentEvent.value.endDate + currentEvent.value.endTime
        )
    }

    fun updateStartTime(value: Long) {
        _currentEvent.value = currentEvent.value.copy(
            startTime = value,
            isStartDateError = currentEvent.value.startDate + value >=
                    currentEvent.value.endDate + currentEvent.value.endTime,
            isEndDateError = currentEvent.value.startDate + value >=
                    currentEvent.value.endDate + currentEvent.value.endTime,
        )
    }

    fun updateEndDate(value: Long) {
        _currentEvent.value = currentEvent.value.copy(
            endDate = value,
            isStartDateError = currentEvent.value.startDate + currentEvent.value.startTime >=
                    value + currentEvent.value.endTime,
            isEndDateError = currentEvent.value.startDate + currentEvent.value.startTime >=
                    value + currentEvent.value.endTime
        )
    }

    fun updateEndTime(value: Long) {
        _currentEvent.value = currentEvent.value.copy(
            endTime = value,
            isStartDateError = currentEvent.value.startDate + currentEvent.value.startTime >=
                    currentEvent.value.endDate + value,
            isEndDateError = currentEvent.value.startDate + currentEvent.value.startTime >=
                    currentEvent.value.endDate + value
        )
    }

    fun updateNote(note: String) {
        _currentEvent.value = currentEvent.value.copy(
            note = note
        )
    }

    fun updateAllDayState(state: Boolean) {
        if (state) {
            _currentEvent.value = currentEvent.value.copy(
                startTime = 0L,
                endTime = 0L
            )
        }
        _currentEvent.value = currentEvent.value.copy(
            allDay = state
        )
    }

    fun updateRepeat(repeat: Long) {
        _currentEvent.value = currentEvent.value.copy(
            repeat = repeat
        )
    }

    fun updateRepeatTitle(@StringRes value: Int) {
        _currentEvent.value = currentEvent.value.copy(
            repeatTitle = value
        )
    }

    fun updateNotificationTitle(title: IntArray) {
        _currentEvent.value = currentEvent.value.copy(
            notificationTitle = title.toList()
        )
    }

    fun updateNotifications(notifications: LongArray) {
        _currentEvent.value = currentEvent.value.copy(
            notifications = notifications.toList()
        )
    }

    fun updateTitle(title: String) {
        val currentText = currentEvent.value.title.text
        if (title != currentText) {
            _currentEvent.value = currentEvent.value.copy(
                title = currentEvent.value.title.copy(
                    text = title,
                    isEmpty = title.isEmpty()
                )
            )
            if (title.isNotEmpty()) {
                _currentEvent.value = currentEvent.value.copy(
                    title = currentEvent.value.title.copy(
                        isError = false
                    )
                )
            }
        }
    }

    fun deleteEvent() {
        if (preEditEvent != Event()) {
            viewModelScope.launch {
                eventUseCases.deleteEventUseCase(
                    preEditEvent
                )
                _eventFlow.emit(UiEvent.Delete)
            }
        }
    }

    fun saveEvent() {
        viewModelScope.launch {
            if (!currentEvent.value.title.isEmpty
                && !currentEvent.value.isStartDateError
                && !currentEvent.value.isEndDateError
            ) {
                if (currentEvent.value.id == null) {
                    val id = eventUseCases.createEventUseCase(
                        Event(
                            title = currentEvent.value.title.text,
                            startDateTime = Date(currentEvent.value.startDate + currentEvent.value.startTime),
                            endDateTime = Date(currentEvent.value.endDate + currentEvent.value.endTime),
                            allDay = currentEvent.value.allDay,
                            color = currentEvent.value.color,
                            completed = currentEvent.value.completed,
                            location = currentEvent.value.location,
                            note = currentEvent.value.note,
                            cover = currentEvent.value.chosenCover.toString()
                        )
                    )
                    eventUseCases.createRepeatUseCase(
                        Repeat(
                            repeatStart = Date(currentEvent.value.startDate + currentEvent.value.startTime),
                            repeatInterval = currentEvent.value.repeat,
                            eventOwnerId = id.toInt()
                        )
                    )
                    val list = mutableListOf<Attachment>()
                    currentEvent.value.attachments.forEach {
                        list.add(
                            Attachment(
                                uri = it.uri,
                                type = it.type,
                                size = it.size,
                                duration = it.duration,
                                eventOwnerId = id.toInt()
                            )
                        )
                    }
                    eventUseCases.createAttachmentsUseCase(list)
                } else {
                    eventUseCases.createEventUseCase(
                        Event(
                            eventId = currentEvent.value.id,
                            title = currentEvent.value.title.text,
                            startDateTime = Date(currentEvent.value.startDate + currentEvent.value.startTime),
                            endDateTime = Date(currentEvent.value.endDate + currentEvent.value.endTime),
                            allDay = currentEvent.value.allDay,
                            color = currentEvent.value.color,
                            completed = currentEvent.value.completed,
                            location = currentEvent.value.location,
                            note = currentEvent.value.note,
                            cover = currentEvent.value.chosenCover.toString()
                        )
                    )
                    eventUseCases.createRepeatUseCase(
                        Repeat(
                            repeatStart = Date(currentEvent.value.startDate + currentEvent.value.startTime),
                            repeatInterval = currentEvent.value.repeat,
                            eventOwnerId = currentEvent.value.id!!
                        )
                    )
                    val list = mutableListOf<Attachment>()
                    currentEvent.value.attachments.forEach {
                        list.add(
                            Attachment(
                                uri = it.uri,
                                type = it.type,
                                size = it.size,
                                duration = it.duration,
                                eventOwnerId = currentEvent.value.id!!
                            )
                        )
                    }
                    eventUseCases.createAttachmentsUseCase(list)
                }
                _eventFlow.emit(UiEvent.Save)
            } else {
                if (currentEvent.value.title.isEmpty) {
                    _currentEvent.value = currentEvent.value.copy(
                        title = currentEvent.value.title.copy(isError = true)
                    )
                }
            }
        }
    }

    private fun getURLForResource(resourceId: Int): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                    + BuildConfig.APPLICATION_ID + "/drawable/" + resourceId
        )
    }

    fun updateCover(uri: Uri) {
        _currentEvent.update { currentState ->
            currentState.copy(
                chosenCover = uri,
            )
        }
    }

    fun updateImages(imagePathList: List<Uri>) {
        _currentEvent.update { currentState ->
            currentState.copy(
                images = imagePathList.toList()
            )
        }
    }

    fun updateVideos(videoPathList: MutableList<VideoItemState>) {
        _currentEvent.update { currentState ->
            currentState.copy(
                videos = videoPathList.toList()
            )
        }
    }

    fun updateChosenImage(items: List<Uri>) {
        _currentEvent.update { currentState ->
            currentState.copy(
                chosenImages = items.toList()
            )
        }
    }

    fun updateChosenVideo(items: List<VideoItemState>) {
        _currentEvent.update { currentState ->
            currentState.copy(
                chosenVideos = items.toList()
            )
        }
    }

    fun updateAttachment() {
        val temp = mutableListOf<Attachment>()
        currentEvent.value.chosenImages.forEach { uri ->
            temp.add(
                Attachment(
                    uri = uri.toString(),
                    type = Attachment.IMAGE
                )
            )
        }

        currentEvent.value.chosenVideos.forEach { item ->
            temp.add(
                Attachment(
                    uri = item.uri.toString(),
                    type = Attachment.VIDEO,
                    size = item.size,
                    duration = item.duration
                )
            )
        }
        _currentEvent.update { currentState ->
            currentState.copy(
                attachments = temp
            )
        }
    }
}