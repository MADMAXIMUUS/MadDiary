package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.ListEventAdapter

class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root),
    MainListHolder {

    private var listener: ListEventAdapter.OnItemClickListener? = null

    override fun bind(item: MainScreenItem) {
        val eventItem = item as MainScreenItem.EventItem
        binding.apply {
            if (eventItem.isNotificationSet) {
                itemEventReminderIcon.visibility = View.VISIBLE
            }
            itemEventTitle.text = eventItem.title
            if (eventItem.address.isNotEmpty()) {
                itemEventAddress.visibility = View.VISIBLE
                itemEventAddress.text = eventItem.address
            } else {
                itemEventAddress.visibility = View.GONE
            }
            if (eventItem.startTime.isNotEmpty()) {
                itemEventStartTime.text = eventItem.startTime
                itemEventStartTime.visibility = View.VISIBLE
                itemEventStartTitle.visibility = View.VISIBLE
            } else {
                itemEventStartTime.visibility = View.GONE
                itemEventStartTitle.visibility = View.GONE
            }
            if (eventItem.endTime.isNotEmpty()) {
                itemEventEndTime.text = eventItem.endTime
                itemEventEndTime.visibility = View.VISIBLE
                itemEventEndTitle.visibility = View.VISIBLE
            } else {
                itemEventEndTime.visibility = View.GONE
                itemEventEndTitle.visibility = View.GONE
            }
            root.setOnClickListener {
                listener?.onClick(eventItem.id, eventItem.chapter, eventItem.chapters)
            }
        }
    }

    override fun onAttach(listener: ListEventAdapter.OnItemClickListener) {
        this.listener = listener
    }

    override fun onDetach() {
        this.listener = null
    }

}