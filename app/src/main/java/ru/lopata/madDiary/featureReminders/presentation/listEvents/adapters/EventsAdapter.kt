package ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.databinding.ItemEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.EventItem

class EventsAdapter(private val clickListener: OnItemClickListener) :
    DelegateAdapter<EventItem, EventsAdapter.EventViewHolder>(EventItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun bindViewHolder(
        model: EventItem,
        viewHolder: EventViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        when (val payload = payloads.firstOrNull() as? EventItem.ChangePayload) {
            is EventItem.ChangePayload.CompletedChange -> viewHolder.bindChecked(
                payload.isCompleted,
                payload.pass
            )
            else ->
                viewHolder.bind(model)
        }
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindChecked(isChecked: Boolean, pass: Boolean) {
            binding.apply {
                if (isChecked) {
                    itemEventTitle.paintFlags = itemEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemEventTitle.isEnabled = false
                } else {
                    itemEventTitle.paintFlags = itemEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    if (!pass)
                        itemEventTitle.isEnabled = true
                }
            }
        }

        fun bind(item: EventItem) {
            binding.apply {

                if (item.pass) {
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

                if (item.isNotificationSet) {
                    itemEventNotificationIcon.visibility = View.VISIBLE
                } else {
                    itemEventNotificationIcon.visibility = View.GONE
                }
                itemEventTitle.text = item.title

                if (item.address.isNotEmpty()) {
                    itemEventAddress.visibility = View.VISIBLE
                    itemEventAddress.text = item.address
                } else {
                    itemEventAddress.visibility = View.GONE
                }
                if (item.startTime.isNotEmpty()) {
                    itemEventStartTime.text = item.startTime
                    itemEventStartTime.visibility = View.VISIBLE
                    itemEventStartTitle.text = itemView.context.getString(item.subtitleFrom)
                    itemEventStartTitle.visibility = View.VISIBLE
                } else {
                    itemEventStartTime.visibility = View.GONE
                    itemEventStartTitle.visibility = View.GONE
                }
                if (item.endTime.isNotEmpty() && item.type == Event.Types.EVENT) {
                    itemEventEndTime.text = item.endTime
                    itemEventEndTime.visibility = View.VISIBLE
                    itemEventStartTitle.text = itemView.context.getString(item.subtitleFrom)
                    itemEventEndTitle.visibility = View.VISIBLE
                } else {
                    itemEventEndTime.visibility = View.GONE
                    itemEventEndTitle.visibility = View.GONE
                }
                if (item.type == Event.Types.TASK) {
                    itemEventCb.isChecked = item.isChecked
                    if (item.isChecked) {
                        itemEventTitle.paintFlags = itemEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        itemEventTitle.isEnabled = false
                    } else {
                        itemEventTitle.paintFlags = itemEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        if (!item.pass)
                            itemEventTitle.isEnabled = true
                    }
                    itemEventCb.backgroundTintList = ColorStateList.valueOf(item.color)
                    itemEventCb.visibility = View.VISIBLE
                    itemEventColor.visibility = View.INVISIBLE
                    itemEventCb.setOnCheckedChangeListener { _, isChecked ->
                        clickListener.onItemCheckedClick(item.id, isChecked)
                    }
                } else {
                    itemEventColor.backgroundTintList = ColorStateList.valueOf(item.color)
                    itemEventCb.visibility = View.INVISIBLE
                    itemEventColor.visibility = View.VISIBLE
                }
                if (item.cover != Uri.EMPTY) {
                    itemEventCover.visibility = View.VISIBLE
                    Glide
                        .with(itemEventCover.context)
                        .load(item.cover)
                        .into(itemEventCover)
                } else {
                    itemEventCover.visibility = View.GONE
                }
                if (item.isAttachmentAdded)
                    itemEventAttachmentIcon.visibility = View.VISIBLE
                else
                    itemEventAttachmentIcon.visibility = View.GONE

                root.setOnClickListener {
                    clickListener.onItemClick(
                        item.id,
                        item.chapter,
                        item.chapters,
                        item.type
                    )
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int, chapter: Int, chapters: Int, type: Event.Types)
        fun onItemCheckedClick(id: Int, state: Boolean)
    }
}