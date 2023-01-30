package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.core.util.toTimeDuration
import ru.lopata.madDiary.databinding.ItemAudioBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.AudioItemState

class AudioAdapter(val listener: OnAttachmentDialogListener) :
    ListAdapter<AudioItemState, AudioAdapter.AudioViewHolder>(DiffCallback()) {

    inner class AudioViewHolder(val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: AudioItemState) {
            binding.itemAudioName.text = item.name
            binding.itemAudioDuration.text = item.duration.toTimeDuration()
            binding.itemAudioPlayPauseButton.setOnClickListener {
                listener.onAudioDialogShow(item, false)
            }
            binding.root.setOnClickListener {
                listener.onAudioChosen(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = ItemAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<AudioItemState>() {
        override fun areItemsTheSame(oldItem: AudioItemState, newItem: AudioItemState): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: AudioItemState, newItem: AudioItemState) =
            oldItem == newItem
    }
}