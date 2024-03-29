package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.lopata.madDiary.R
import ru.lopata.madDiary.databinding.ItemAttachBinding
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.domain.model.states.AudioItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.ImageItemState
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState

class AttachmentAdapter(
    private val listener: OnAttachmentDialogListener? = null
) : ListAdapter<Attachment, AttachmentAdapter.AttachmentViewHolder>(DiffCallback()) {

    inner class AttachmentViewHolder(val binding: ItemAttachBinding) : ViewHolder(binding.root) {

        fun onBind(item: Attachment) {
            binding.apply {
                when (item.type) {

                    Attachment.IMAGE -> {
                        Glide.with(attachIcon.context).load(item.uri).into(attachIcon)
                        attachIcon.scaleType = ImageView.ScaleType.CENTER_CROP
                        attachPlay.visibility = View.GONE
                        attachTitle.visibility = View.GONE

                        if (listener != null) {
                            root.setOnClickListener {
                                listener.onImageDialogShow(
                                    item = ImageItemState(
                                        uri = Uri.parse(item.uri), size = item.size
                                    ), isChosen = true
                                )
                            }
                        }
                    }

                    Attachment.VIDEO -> {
                        Glide.with(attachIcon.context).load(item.uri).into(attachIcon)
                        attachIcon.scaleType = ImageView.ScaleType.CENTER_CROP
                        attachPlay.visibility = View.VISIBLE
                        attachTitle.visibility = View.GONE

                        if (listener != null) {
                            root.setOnClickListener {
                                listener.onVideoDialogShow(
                                    item = VideoItemState(
                                        uri = Uri.parse(item.uri),
                                        duration = item.duration,
                                        size = item.size
                                    ), isChosen = true
                                )
                            }
                        }
                    }

                    Attachment.AUDIO -> {
                        attachIcon.setImageResource(R.drawable.ic_attachment_audio)
                        attachIcon.scaleType = ImageView.ScaleType.CENTER_CROP
                        attachPlay.visibility = View.GONE
                        attachTitle.text = item.name
                        attachTitle.visibility = View.VISIBLE

                        if (listener != null) {
                            root.setOnClickListener {
                                listener.onAudioDialogShow(
                                    item = AudioItemState(
                                        uri = Uri.parse(item.uri),
                                        duration = item.duration,
                                        name = item.name,
                                        size = item.size
                                    ), isChosen = true
                                )
                            }
                        }
                    }

                    Attachment.FILE -> {
                        attachIcon.setImageResource(R.drawable.ic_attachment_file)
                        attachTitle.visibility = View.VISIBLE
                        attachTitle.text = item.name
                        attachIcon.scaleType = ImageView.ScaleType.CENTER_CROP
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
        holder.onBind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<Attachment>() {
        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment) =
            oldItem == newItem
    }

}