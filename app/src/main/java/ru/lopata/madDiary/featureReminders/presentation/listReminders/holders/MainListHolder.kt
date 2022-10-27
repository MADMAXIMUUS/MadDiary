package ru.lopata.madDiary.featureReminders.presentation.listReminders.holders

import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem

interface MainListHolder {
    fun bind(item: MainScreenItem)
    fun onAttach()
    fun onDetach()
}