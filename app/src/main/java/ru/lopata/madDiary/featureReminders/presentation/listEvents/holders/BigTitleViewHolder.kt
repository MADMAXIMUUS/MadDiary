package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemBigTitleBinding
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.ListEventAdapter

class BigTitleViewHolder(val binding: ItemBigTitleBinding) : RecyclerView.ViewHolder(binding.root),
    MainListHolder {

    @SuppressLint("SetTextI18n")
    override fun bind(item: MainScreenItem) {
        val monthYearTitleItem = item as MainScreenItem.MonthYearTitleItem
        binding.itemBigTitleTv.text = "${monthYearTitleItem.month} ${monthYearTitleItem.yearNumber}"
    }

    override fun onAttach(listener: ListEventAdapter.OnItemClickListener) {

    }

    override fun onDetach() {

    }
}