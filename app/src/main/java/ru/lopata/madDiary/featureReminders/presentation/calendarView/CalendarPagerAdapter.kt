package ru.lopata.madDiary.featureReminders.presentation.calendarView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.core.util.CalendarGridDecoration
import ru.lopata.madDiary.databinding.CalendarGridBinding

class CalendarPagerAdapter(
    private val context: Context
) : ListAdapter<Month, CalendarPagerAdapter.CalendarPagerHolder>(DiffCallback()) {

    inner class CalendarPagerHolder(
        private val binding: CalendarGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(month: Month) {
            val gridAdapter = CalendarGridAdapter()
            binding.calendarGrid.apply {
                adapter = gridAdapter
                layoutManager = object : GridLayoutManager(this@CalendarPagerAdapter.context, 7) {
                    override fun canScrollVertically(): Boolean = false
                    override fun canScrollHorizontally(): Boolean = false
                }
                addItemDecoration(CalendarGridDecoration())
            }
            gridAdapter.submitList(month.days)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Month>() {
        override fun areItemsTheSame(oldItem: Month, newItem: Month) =
            oldItem.yearMonth == newItem.yearMonth

        override fun areContentsTheSame(oldItem: Month, newItem: Month) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPagerHolder {
        val binding = CalendarGridBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarPagerHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarPagerHolder, position: Int) {
        val month = getItem(position)
        holder.bind(month)
    }

}