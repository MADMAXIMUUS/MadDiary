package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemEventBinding
import ru.lopata.madDiary.databinding.ItemTitleBinding
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem

class HolderFactory {
    companion object {
        fun getHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                MainScreenItem.TITLE -> {
                    val binding = ItemTitleBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                    TitleViewHolder(binding)
                }
                MainScreenItem.EVENT -> {
                    val binding = ItemEventBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                    EventViewHolder(binding)
                }
                else -> {
                    val binding = ItemEventBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                    EventViewHolder(binding)
                }
            }
        }
    }
}