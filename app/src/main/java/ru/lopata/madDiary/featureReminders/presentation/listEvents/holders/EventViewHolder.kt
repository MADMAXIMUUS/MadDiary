package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.databinding.ItemEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.ListEventAdapter

class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root),
    MainListHolder {

    private var listener: ListEventAdapter.OnItemClickListener? = null

    override fun bind(item: MainScreenItem) {
        val eventItem = item as MainScreenItem.EventItem
        binding.apply {

            if (eventItem.pass) {
                itemEventTitle.isEnabled = false
                itemEventStartTime.isEnabled = false
                itemEventStartTitle.isEnabled = false
                itemEventEndTime.isEnabled = false
                itemEventEndTitle.isEnabled = false
                itemEventAttachmentIcon.isEnabled = false
                itemEventNotificationIcon.isEnabled = false
            } else {
                itemEventTitle.isEnabled = true
                itemEventStartTime.isEnabled = true
                itemEventStartTitle.isEnabled = true
                itemEventEndTime.isEnabled = true
                itemEventEndTitle.isEnabled = true
                itemEventAttachmentIcon.isEnabled = true
                itemEventNotificationIcon.isEnabled = true
            }

            if (eventItem.isNotificationSet) {
                itemEventNotificationIcon.visibility = View.VISIBLE
            } else {
                itemEventNotificationIcon.visibility = View.GONE
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
                itemEventStartTitle.text = itemView.context.getString(eventItem.subtitleFrom)
                itemEventStartTitle.visibility = View.VISIBLE
            } else {
                itemEventStartTime.visibility = View.GONE
                itemEventStartTitle.visibility = View.GONE
            }
            if (eventItem.endTime.isNotEmpty() && eventItem.type == Event.Types.EVENT) {
                itemEventEndTime.text = eventItem.endTime
                itemEventEndTime.visibility = View.VISIBLE
                itemEventStartTitle.text = itemView.context.getString(eventItem.subtitleFrom)
                itemEventEndTitle.visibility = View.VISIBLE
            } else {
                itemEventEndTime.visibility = View.GONE
                itemEventEndTitle.visibility = View.GONE
            }
            if (eventItem.type == Event.Types.TASK) {
                itemEventCb.isChecked = eventItem.isChecked
                if (eventItem.isChecked) {
                    itemEventTitle.paintFlags =
                        itemEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemEventTitle.isEnabled = false
                } else {
                    itemEventTitle.paintFlags =
                        itemEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    if (!eventItem.pass)
                        itemEventTitle.isEnabled = true
                }
                itemEventCb.backgroundTintList = ColorStateList.valueOf(eventItem.color)
                itemEventCb.visibility = View.VISIBLE
                itemEventColor.visibility = View.INVISIBLE
                itemEventCb.setOnCheckedChangeListener { _, isChecked ->
                    listener?.onItemCheckedClick(eventItem.id, isChecked)
                }
            } else {
                itemEventColor.backgroundTintList = ColorStateList.valueOf(eventItem.color)
                itemEventCb.visibility = View.INVISIBLE
                itemEventColor.visibility = View.VISIBLE
            }
            if (eventItem.cover != Uri.EMPTY) {
                itemEventCover.visibility = View.VISIBLE
                Glide
                    .with(itemEventCover.context)
                    .load(eventItem.cover)
                    .into(itemEventCover)
            } else {
                itemEventCover.visibility = View.GONE
            }
            if (eventItem.isAttachmentAdded)
                itemEventAttachmentIcon.visibility = View.VISIBLE
            else
                itemEventAttachmentIcon.visibility = View.GONE

            root.setOnClickListener {
                listener?.onItemClick(
                    eventItem.id,
                    eventItem.chapter,
                    eventItem.chapters,
                    eventItem.type
                )
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