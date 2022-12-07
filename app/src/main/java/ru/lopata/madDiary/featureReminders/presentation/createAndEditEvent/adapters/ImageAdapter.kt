package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lopata.madDiary.databinding.ItemImageBinding
import ru.lopata.madDiary.featureReminders.domain.model.Attachment
import ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.states.VideoItemState

class ImageAdapter(val listener: OnAttachmentChosenListener) :
    ListAdapter<Uri, ImageAdapter.ImageViewHolder>(DiffCallback()) {

    private var chosenImageUris: MutableList<Uri> = mutableListOf()

    inner class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(uri: Uri) {
            binding.chosenPhotoCb.isChecked = uri in chosenImageUris
            Glide
                .with(binding.photo.context)
                .load(uri)
                .into(binding.photo)

            binding.photo.setOnClickListener {
                if (binding.chosenPhotoCb.isChecked) {
                    chosenImageUris.remove(uri)
                    binding.chosenPhotoCb.isChecked = false
                } else {
                    chosenImageUris.add(uri)
                    binding.chosenPhotoCb.isChecked = true
                }
                listener.onImagesChosen(chosenImageUris)
            }
        }
    }

    fun updateChosen(uris: List<Uri>) {
        chosenImageUris.clear()
        chosenImageUris.addAll(uris)
    }

    fun getChosenUris(): List<Uri> {
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

    class DiffCallback : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri) =
            oldItem == newItem
    }
}