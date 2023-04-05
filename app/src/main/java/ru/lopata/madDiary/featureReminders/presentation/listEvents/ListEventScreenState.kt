package ru.lopata.madDiary.featureReminders.presentation.listEvents

import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem

data class ListEventScreenState(
    val events: List<DelegateAdapterItem> = emptyList(),
    val todayId: Int = 0
)
