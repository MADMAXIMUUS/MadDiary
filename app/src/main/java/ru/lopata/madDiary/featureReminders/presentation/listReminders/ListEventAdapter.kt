package ru.lopata.madDiary.featureReminders.presentation.listReminders

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listReminders.holders.HolderFactory
import ru.lopata.madDiary.featureReminders.presentation.listReminders.holders.MainListHolder

class ListEventAdapter : ListAdapter<MainScreenItem, ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return HolderFactory.getHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as MainListHolder).bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MainScreenItem.TitleItem -> MainScreenItem.TITLE
            is MainScreenItem.EventItem -> MainScreenItem.EVENT
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MainScreenItem>() {
        override fun areItemsTheSame(oldItem: MainScreenItem, newItem: MainScreenItem): Boolean {
            if (oldItem is MainScreenItem.EventItem && newItem is MainScreenItem.EventItem) {
                return (oldItem.id == newItem.id)
            } else if (oldItem is MainScreenItem.TitleItem && newItem is MainScreenItem.TitleItem) {
                return (oldItem.date == newItem.date) && (oldItem.title == newItem.title)
            } else {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }

        override fun areContentsTheSame(oldItem: MainScreenItem, newItem: MainScreenItem) =
            oldItem == newItem
    }
}