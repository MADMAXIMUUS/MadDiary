package ru.madmax.madDiary.featureReminders.presentation.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.madmax.madDiary.databinding.CalendarCellBinding
import ru.madmax.madDiary.featureReminders.domain.model.CalendarModel
import ru.madmax.madDiary.featureReminders.presentation.calendar.CalendarAdapter.CalendarViewHolder
import java.time.LocalDate

class CalendarAdapter(
    private val onDayClickListener: (CalendarModel) -> Unit
) : ListAdapter<CalendarModel, CalendarViewHolder>(DiffCallback()) {

    inner class CalendarViewHolder(
        private val binding: CalendarCellBinding,
        val onDayClickListener: (CalendarModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarModel: CalendarModel) {
            binding.apply {
                if (calendarModel.day == LocalDate.MIN)
                    cellDayText.text = ""
                else {
                    cellDayText.text = calendarModel.day.dayOfMonth.toString()
                    if (calendarModel.isSelectedDay)
                        cellBorder.visibility = View.VISIBLE
                    else
                        cellBorder.visibility = View.GONE
                    if (!calendarModel.isCurrentMonth) {
                        cellDayText.isEnabled = false
                    }
                }
                root.setOnClickListener {
                    onDayClickListener(calendarModel)
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<CalendarModel>() {
        override fun areItemsTheSame(oldItem: CalendarModel, newItem: CalendarModel) =
            oldItem.day == newItem.day

        override fun areContentsTheSame(oldItem: CalendarModel, newItem: CalendarModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = binding.root.layoutParams
        layoutParams.height = (parent.height * 0.16393).toInt()
        return CalendarViewHolder(binding, onDayClickListener)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val calendarModel = getItem(position)
        holder.bind(calendarModel)
    }
}