package ru.lopata.madDiary.featureReminders.presentation.calendarView

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.CalendarGridBinding

class CalendarPagerAdapter(

) : ListAdapter<,CalendarPagerAdapter.CalendarPagerHolder>() {


    internal class CalendarPagerHolder(calendarGrid: CalendarGridBinding) :
        RecyclerView.ViewHolder(calendarGrid.root) {
    }
}