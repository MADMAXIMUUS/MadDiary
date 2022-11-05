package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.ListEventAdapter

interface MainListHolder {
    fun bind(item: MainScreenItem)
    fun onAttach(listener: ListEventAdapter.OnItemClickListener)
    fun onDetach()
}