package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.databinding.ItemImageBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.ImageItemState

class ImageAdapter(val listener: OnAttachmentChosenListener) :
    ListAdapter<ImageItemState, ImageAdapter.ImageViewHolder>(DiffCallback()) {

    private var chosenImageUris: MutableList<ImageItemState> = mutableListOf()

    inner class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: ImageItemState) {
            binding.chosenPhotoCb.isChecked = item in chosenImageUris
            Glide
                .with(binding.photo.context)
                .load(item.uri)
                .into(binding.photo)

            binding.photo.setOnClickListener {
                if (binding.chosenPhotoCb.isChecked) {
                    chosenImageUris.remove(item)
                    binding.chosenPhotoCb.isChecked = false
                } else {
                    chosenImageUris.add(item)
                    binding.chosenPhotoCb.isChecked = true
                }
                listener.onImagesChosen(chosenImageUris)
            }
        }
    }

    fun updateChosen(items: List<ImageItemState>) {
        chosenImageUris.clear()
        chosenImageUris.addAll(items)
        currentList.forEachIndexed { index, item ->
            if (item in items) {
                notifyItemChanged(index)
            }
        }
    }

    fun getChosenUris(): List<ImageItemState> {
        return chosenImageUris
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }

    class DiffCallback : DiffUtil.ItemCallback<ImageItemState>() {
        override fun areItemsTheSame(oldItem: ImageItemState, newItem: ImageItemState): Boolean =
            oldItem.uri == newItem.uri

        override fun areContentsTheSame(oldItem: ImageItemState, newItem: ImageItemState) =
            oldItem == newItem
    }
}