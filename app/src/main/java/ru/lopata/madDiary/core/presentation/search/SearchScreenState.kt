package ru.lopata.madDiary.core.presentation.search

import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem

data class SearchScreenState(
    val searchQuery: String = "",
    val searchResults: List<DelegateAdapterItem> = emptyList()
)
