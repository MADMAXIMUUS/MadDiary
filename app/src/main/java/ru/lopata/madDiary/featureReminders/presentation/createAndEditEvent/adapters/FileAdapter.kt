package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemFileBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.FileItemState

class FileAdapter(val listener: OnAttachmentDialogListener) :
    ListAdapter<FileItemState, FileAdapter.ImageViewHolder>(DiffCallback()) {

    inner class ImageViewHolder(val binding: ItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: FileItemState) {
            binding.itemFileName.text = item.name
            binding.itemFileSize.text = item.sizeTitle
            binding.root.setOnClickListener {
                listener.onFileChosen(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<FileItemState>() {
        override fun areItemsTheSame(oldItem: FileItemState, newItem: FileItemState): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: FileItemState, newItem: FileItemState) =
            oldItem == newItem
    }
}