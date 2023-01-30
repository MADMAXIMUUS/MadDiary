package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.databinding.ItemImageBinding
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.ImageItemState

class ImageAdapter(
    private val listener: OnAttachmentDialogListener
) : ListAdapter<ImageItemState, ImageAdapter.ImageViewHolder>(DiffCallback()) {

    private var chosenImageUris: MutableList<ImageItemState> = mutableListOf()

    inner class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isChecked: Boolean = false

        fun onBind(item: ImageItemState) {
            isChecked = item in chosenImageUris

            if (isChecked) {
                binding.chosenPhotoCb.speed = 2f
                binding.chosenPhotoCb.setMinAndMaxProgress(0f, 0.5f)
                binding.chosenPhotoCb.playAnimation()
            } else {
                binding.chosenPhotoCb.progress = 0f
            }

            Glide
                .with(binding.photo.context)
                .load(item.uri)
                .into(binding.photo)

            binding.chosenPhotoCb.setOnClickListener {
                if (!isChecked) {
                    chosenImageUris.add(item)
                    binding.chosenPhotoCb.speed = 2f
                    binding.chosenPhotoCb.setMinAndMaxProgress(0f, 0.5f)
                    binding.chosenPhotoCb.playAnimation()
                    isChecked = true
                } else {
                    chosenImageUris.remove(item)
                    binding.chosenPhotoCb.speed = 2f
                    binding.chosenPhotoCb.setMinAndMaxProgress(0.5f, 1.0f)
                    binding.chosenPhotoCb.playAnimation()
                    isChecked = false
                }
                listener.onImagesChosen(chosenImageUris)
            }

            binding.photo.setOnClickListener {
                listener.onImageDialogShow(item, isChecked)
            }
        }
    }

    fun updateChosen(items: List<ImageItemState>) {
        val oldList = chosenImageUris.toList()
        Log.e("MediaPreview", "Before: $oldList")
        chosenImageUris.clear()
        chosenImageUris.addAll(items)
        Log.e("MediaPreview", "After: $chosenImageUris")
        currentList.forEachIndexed { index, item ->
            if (item in items) {
                notifyItemChanged(index)
            }else if (item in oldList) {
                notifyItemChanged(index)
            }
        }
    }

    fun getChosenUris() = chosenImageUris

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