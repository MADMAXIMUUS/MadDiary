package ru.lopata.madDiary.featureReminders.presentation.dialogs.modal.adapter

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemCalendarEventBinding
import ru.lopata.madDiary.featureReminders.domain.model.Event
import ru.lopata.madDiary.featureReminders.presentation.calendarScreen.states.EventInCalendarGrid

class CalendarEventDialogAdapter(
    private val listener: OnEventClickListener
) :
    ListAdapter<EventInCalendarGrid, CalendarEventDialogAdapter.EventHolder>(DiffCallback()) {

    inner class EventHolder(private val binding: ItemCalendarEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EventInCalendarGrid) {
            binding.apply {
                itemEventTitle.text = item.title
                when (item.type) {
                    Event.Types.EVENT -> {
                        itemEventCb.visibility = View.INVISIBLE
                        itemEventColor.visibility = View.VISIBLE
                        itemEventColor.setCardBackgroundColor(item.color)
                    }
                    Event.Types.TASK -> {
                        itemEventCb.backgroundTintList = ColorStateList.valueOf(item.color)
                        itemEventCb.visibility = View.VISIBLE
                        itemEventColor.visibility = View.INVISIBLE
                        itemEventCb.isChecked = item.isChecked
                        if (item.isChecked) {
                            itemEventTitle.paintFlags =
                                itemEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            itemEventTitle.isEnabled = false
                        } else {
                            itemEventTitle.paintFlags =
                                itemEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                            itemEventTitle.isEnabled = true
                        }
                        itemEventCb.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                itemEventTitle.paintFlags =
                                    itemEventTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                itemEventTitle.isEnabled = false
                            } else {
                                itemEventTitle.paintFlags =
                                    itemEventTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                itemEventTitle.isEnabled = true
                            }
                            listener.onCheckClick(item.id, isChecked)
                        }
                    }
                    Event.Types.REMINDER -> {
                        itemEventCb.visibility = View.INVISIBLE
                        itemEventColor.visibility = View.VISIBLE
                        itemEventColor.setCardBackgroundColor(item.color)
                    }
                }
                itemEventTitle.isEnabled = !item.pass
                root.setOnClickListener {
                    listener.onClick(item.id, item.type)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val binding =
            ItemCalendarEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventHolder(binding)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val month = getItem(position)
        holder.bind(month)
    }

    class DiffCallback : DiffUtil.ItemCallback<EventInCalendarGrid>() {
        override fun areItemsTheSame(oldItem: EventInCalendarGrid, newItem: EventInCalendarGrid) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: EventInCalendarGrid,
            newItem: EventInCalendarGrid
        ) =
            oldItem == newItem
    }

    interface OnEventClickListener {
        fun onClick(eventId: Int, type: Event.Types)
        fun onCheckClick(eventId: Int, state: Boolean)
    }

}