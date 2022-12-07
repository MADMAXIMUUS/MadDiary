package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.core.util.toTimeDuration
import ru.lopata.madDiary.databinding.ItemVideoBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState

class VideoAdapter(val listener: OnAttachmentChosenListener) :
    ListAdapter<VideoItemState, VideoAdapter.VideoViewHolder>(DiffCallback()) {

    private var chosenVideoUris: MutableList<VideoItemState> = mutableListOf()

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: VideoItemState) {
            binding.chosenVideoCb.isChecked = item in chosenVideoUris
            Glide
                .with(binding.thumbnail.context)
                .load(item.uri)
                .into(binding.thumbnail)
            binding.duration.text = item.duration.toTimeDuration()
            binding.thumbnail.setOnClickListener {
                if (binding.chosenVideoCb.isChecked) {
                    chosenVideoUris.remove(item)
                    binding.chosenVideoCb.isChecked = false
                } else {
                    chosenVideoUris.add(item)
                    binding.chosenVideoCb.isChecked = true
                }
                listener.onVideosChosen(chosenVideoUris)
            }
        }
    }

    fun updateChosen(items: List<VideoItemState>) {
        chosenVideoUris.clear()
        chosenVideoUris.addAll(items)
    }

    fun getChosenUris(): List<VideoItemState> {
        return chosenVideoUris
    }

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