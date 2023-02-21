package ru.lopata.madDiary.featureReminders.presentation.listEvents

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.holders.HolderFactory
import ru.lopata.madDiary.featureReminders.presentation.listEvents.holders.MainListHolder

class ListEventAdapter(
    onItemClickListener: OnItemClickListener
) : ListAdapter<MainScreenItem, ViewHolder>(DiffCallback()) {

    private var listener: OnItemClickListener = onItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return HolderFactory.getHolder(parent, viewType)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as MainListHolder).onAttach(listener)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as MainListHolder).onDetach()
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
            return if (oldItem is MainScreenItem.EventItem && newItem is MainScreenItem.EventItem) {
                (oldItem.id == newItem.id)
            } else if (oldItem is MainScreenItem.TitleItem && newItem is MainScreenItem.TitleItem) {
                (oldItem.date == newItem.date) && (oldItem.title == newItem.title)
            } else {
                oldItem.hashCode() == newItem.hashCode()
            }
        }

        override fun areContentsTheSame(oldItem: MainScreenItem, newItem: MainScreenItem) =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int, chapter: Int, chapters: Int, type: Event.Types)
        fun onItemCheckedClick(id: Int, state: Boolean)
    }
}