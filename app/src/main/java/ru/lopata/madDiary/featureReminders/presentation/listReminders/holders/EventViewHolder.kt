package ru.lopata.madDiary.featureReminders.presentation.listReminders.holders

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem

class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root),
    MainListHolder {
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
        }
        Log.e("Event", eventItem.toString())
    }

    override fun onAttach() {

    }

    override fun onDetach() {

    }

}