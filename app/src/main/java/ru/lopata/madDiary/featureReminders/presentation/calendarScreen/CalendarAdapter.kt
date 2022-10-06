package ru.lopata.madDiary.featureReminders.presentation.calendarScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.CalendarCellBinding
import ru.lopata.madDiary.featureReminders.presentation.calendarView.CalendarDate

class CalendarAdapter(
    private val onDayClickListener: (CalendarDate) -> Unit
) : ListAdapter<CalendarDate, CalendarAdapter.CalendarViewHolder>(DiffCallback()) {

    inner class CalendarViewHolder(
        private val binding: CalendarCellBinding,
        val onDayClickListener: (CalendarDate) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calendarDate: CalendarDate) {
            binding.apply {
                /*if (calendarDate.day == LocalDate.MIN)
                    cellDayText.text = ""
                else {
                    cellDayText.text = calendarDate.day.dayOfMonth.toString()
                    if (calendarDate.isSelectedDay)
                        cellBorder.visibility = View.VISIBLE
                    else
                        cellBorder.visibility = View.GONE
                    if (!calendarDate.isCurrentMonth) {
                        cellDayText.isEnabled = false
                    }
                }*/
                root.setOnClickListener {
                    onDayClickListener(calendarDate)
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