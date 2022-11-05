package ru.lopata.madDiary.featureReminders.presentation.listEvents.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemTitleBinding
import ru.lopata.madDiary.featureReminders.domain.model.MainScreenItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.ListEventAdapter

class TitleViewHolder(val binding: ItemTitleBinding) : RecyclerView.ViewHolder(binding.root),
    MainListHolder {

    override fun bind(item: MainScreenItem) {
        val titleItem = item as MainScreenItem.TitleItem
        binding.apply {
            if (titleItem.title != -1) {
                itemTitleDayTitle.text = itemView.context.getString(titleItem.title)
                itemTitleDate.text = titleItem.date
                itemTitleDate.visibility = View.VISIBLE
                itemTitleDivider.visibility = View.VISIBLE
                itemTitleDayTitle.visibility = View.VISIBLE
            }
            if (titleItem.title == -1) {
                itemTitleDayTitle.text = titleItem.date
                itemTitleDivider.visibility = View.GONE
                itemTitleDate.visibility = View.GONE
            }
        }
    }

    override fun onAttach(listener: ListEventAdapter.OnItemClickListener) {

    }

    override fun onDetach() {

    }
}