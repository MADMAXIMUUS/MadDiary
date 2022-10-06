package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.CalendarCellBinding
import ru.lopata.madDiary.featureReminders.util.DayType

class CalendarGridAdapter :
    ListAdapter<CalendarDate, CalendarGridAdapter.CalendarGridHolder>(DiffCallback()) {

    class CalendarGridHolder(
        private val binding: CalendarCellBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(date: CalendarDate) {
            binding.apply {
                cellDayText.text = date.day.dayOfMonth.toString()
                if (date.dateType == DayType.TODAY)
                    cellCurrent.visibility = View.VISIBLE
                else
                    cellCurrent.visibility = View.GONE
                if (date.isSelectedDay)
                    cellBorder.visibility = View.VISIBLE
                else
                    cellBorder.visibility = View.GONE
                if (date.dateType == DayType.NOT_CURRENT_MONTH) {
                    cellDayText.isEnabled = false
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CalendarDate>() {
        override fun areItemsTheSame(oldItem: CalendarDate, newItem: CalendarDate) =
            oldItem.day == newItem.day

        override fun areContentsTheSame(oldItem: CalendarDate, newItem: CalendarDate) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarGridHolder {
        val binding = CalendarCellBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = binding.root.layoutParams
        layoutParams.height = parent.height / 6
        return CalendarGridHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarGridHolder, position: Int) {
        val day = getItem(position)
        holder.bind(day)
    }

}