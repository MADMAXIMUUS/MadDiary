package ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemTitleBinding
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DateItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem

class DateTitleAdapter :
    DelegateAdapter<DateItem, DateTitleAdapter.DateViewHolder>(DateItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemTitleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun bindViewHolder(
        model: DateItem,
        viewHolder: DateViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        viewHolder.bind(model)
    }

    inner class DateViewHolder(private val binding: ItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DateItem) {
            binding.apply {
                if (item.title != -1) {
                    itemTitleDayTitle.text = itemView.context.getString(item.title)
                    itemTitleDate.text = item.date
                    itemTitleDate.visibility = View.VISIBLE
                    itemTitleDivider.visibility = View.VISIBLE
                    itemTitleDayTitle.visibility = View.VISIBLE
                }
                if (item.title == -1) {
                    itemTitleDayTitle.text = item.date
                    itemTitleDivider.visibility = View.GONE
                    itemTitleDate.visibility = View.GONE
                }
                itemTitleDayTitle.isEnabled = !item.pass
                itemTitleDate.isEnabled = !item.pass
                itemTitleDivider.isEnabled = !item.pass
            }
        }
    }
}