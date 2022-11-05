package ru.lopata.madDiary.featureReminders.presentation.listEvents

import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem

data class ListEventScreenState(
    val events: List<MainScreenItem> = emptyList(),
    val todayId: Int = 0
)
