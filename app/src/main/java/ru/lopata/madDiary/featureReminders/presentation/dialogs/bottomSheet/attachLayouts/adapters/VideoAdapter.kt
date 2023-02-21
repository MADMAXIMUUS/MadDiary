package ru.lopata.madDiary.featureReminders.presentation.dialogs.bottomSheet.attachLayouts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.core.util.toTimeDuration
import ru.lopata.madDiary.databinding.ItemVideoBinding
import ru.lopata.madDiary.featureReminders.domain.model.states.VideoItemState

class VideoAdapter(
    private val listener: OnAttachmentDialogListener
) : ListAdapter<VideoItemState, VideoAdapter.VideoViewHolder>(DiffCallback()) {

    private var chosenVideoUris: MutableList<VideoItemState> = mutableListOf()

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isChecked: Boolean = false

        fun onBind(item: VideoItemState) {
            if (isChecked) {
                binding.chosenVideoCb.speed = 1.5f
                binding.chosenVideoCb.setMinAndMaxProgress(0f, 0.5f)
                binding.chosenVideoCb.playAnimation()
            }
            Glide
                .with(binding.thumbnail.context)
                .load(item.uri)
                .into(binding.thumbnail)
            binding.duration.text = item.duration.toTimeDuration()

            binding.chosenVideoCb.setOnClickListener {
                if (!isChecked) {
                    chosenVideoUris.add(item)
                    binding.chosenVideoCb.speed = 2f
                    binding.chosenVideoCb.setMinAndMaxProgress(0f, 0.5f)
                    binding.chosenVideoCb.playAnimation()
                    isChecked = true
                } else {
                    chosenVideoUris.remove(item)
                    binding.chosenVideoCb.speed = 2f
                    binding.chosenVideoCb.setMinAndMaxProgress(0.5f, 1.0f)
                    binding.chosenVideoCb.playAnimation()
                    isChecked = false
                }
                listener.onVideosChosen(chosenVideoUris)
            }
            binding.thumbnail.setOnClickListener {
                listener.onVideoDialogShow(item, isChecked)
            }
        }
    }

    fun updateChosen(items: List<VideoItemState>) {
        chosenVideoUris.clear()
        chosenVideoUris.addAll(items)
        currentList.forEachIndexed { index, item ->
            if (item in items) {
                notifyItemChanged(index)
            }
        }
    }

    fun getChosenItems() = chosenVideoUris

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<VideoItemState>() {
        override fun areItemsTheSame(oldItem: VideoItemState, newItem: VideoItemState): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: VideoItemState, newItem: VideoItemState) =
            oldItem == newItem
    }
}