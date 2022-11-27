package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import android.content.res.ColorStateList
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
            itemEventColor.backgroundTintList = ColorStateList.valueOf(eventItem.color)
            if (eventItem.cover != Uri.EMPTY) {
                itemEventCover.visibility = View.VISIBLE
                Glide
                    .with(itemEventCover.context)
                    .load(eventItem.cover)
                    .into(itemEventCover)
            } else {
                itemEventCover.visibility = View.GONE
            }
            /*itemEventCheckedButton.isChecked = eventItem.isChecked
            itemEventCheckedButton.backgroundTintList = ColorStateList.valueOf(eventItem.color)
            itemEventCheckedButton.setOnCheckedChangeListener { _, isChecked ->
                listener?.onItemCheckedClick(eventItem.id, isChecked)
            }*/
            root.setOnClickListener {
                listener?.onItemClick(eventItem.id, eventItem.chapter, eventItem.chapters)
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