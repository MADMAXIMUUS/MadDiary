package ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.databinding.ItemBigTitleBinding
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.MonthYearTitleItem

class MonthYearAdapter :
    DelegateAdapter<MonthYearTitleItem, MonthYearAdapter.MonthYearViewHolder>(MonthYearTitleItem::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemBigTitleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthYearViewHolder(binding)
    }

    override fun bindViewHolder(
        model: MonthYearTitleItem,
        viewHolder: MonthYearViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        viewHolder.bind(model)
    }

    inner class MonthYearViewHolder(private val binding: ItemBigTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MonthYearTitleItem) {
            binding.itemBigTitleTv.text = "${item.month} ${item.yearNumber}"
            Glide
                .with(binding.itemBigTitleImage.context)
                .load(item.image)
                .into(binding.itemBigTitleImage)
        }
    }
}