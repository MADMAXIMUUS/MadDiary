package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.lopata.madDiary.R
import ru.lopata.madDiary.core.util.EventColors
import ru.lopata.madDiary.databinding.ItemAttachBinding
import ru.lopata.madDiary.featureReminders.domain.model.Attachment

class AttachmentAdapter :
    ListAdapter<Attachment, AttachmentAdapter.AttachmentViewHolder>(DiffCallback()) {


    inner class AttachmentViewHolder(val binding: ItemAttachBinding) : ViewHolder(binding.root) {

        fun onBind(item: Attachment, position: Int) {
            binding.apply {
                when (item.type) {
                    Attachment.IMAGE -> {
                        Glide
                            .with(attachIcon.context)
                            .load(item.uri)
                            .into(attachIcon)
                        attachIcon.imageTintList = null
                        attachIcon.scaleType = ImageView.ScaleType.CENTER_CROP
                        attachPlay.visibility = View.GONE
                    }
                    Attachment.VIDEO -> {
                        Glide
                            .with(attachIcon.context)
                            .load(item.uri)
                            .into(attachIcon)
                        attachIcon.imageTintList = null
                        attachIcon.scaleType = ImageView.ScaleType.CENTER_CROP
                        attachPlay.visibility = View.VISIBLE
                    }
                    Attachment.AUDIO -> {
                        attachIcon.setImageResource(R.drawable.ic_attachment_audio)
                        attachIcon.imageTintList = ColorStateList.valueOf(EventColors.GREEN)
                        attachIcon.scaleType = null
                        attachPlay.visibility = View.GONE
                    }
                    Attachment.FILE -> {
                        attachIcon.setImageResource(R.drawable.ic_attachment_file)
                        attachIcon.imageTintList = ColorStateList.valueOf(EventColors.ORANGE)
                        attachIcon.scaleType = null
                        attachPlay.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val binding = ItemAttachBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AttachmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item, position)
    }

    class DiffCallback : DiffUtil.ItemCallback<Attachment>() {
        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment) =
            oldItem == newItem
    }

}