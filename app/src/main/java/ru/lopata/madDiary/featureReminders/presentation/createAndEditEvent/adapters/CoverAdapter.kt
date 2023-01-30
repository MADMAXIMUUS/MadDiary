package ru.lopata.madDiary.featureReminders.presentation.createAndEditEvent.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import ru.lopata.madDiary.databinding.ItemCoverBinding

class CoverAdapter(val listener: OnAttachmentDialogListener?) :
    ListAdapter<Uri, CoverAdapter.CoverViewHolder>(DiffCallback()) {

    private var chosenCover: MaterialCardView? = null
    private var chosenCoverUri: Uri = Uri.EMPTY

    inner class CoverViewHolder(val binding: ItemCoverBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(uri: Uri) {
            binding.root.strokeWidth = 0
            if (chosenCoverUri == uri) {
                chosenCover = binding.root
                binding.root.strokeWidth = 5
            }
            Glide
                .with(binding.coverImage.context)
                .load(uri)
                .into(binding.coverImage)

            binding.coverImage.setOnClickListener {
                if (chosenCover == binding.root && binding.root.strokeWidth == 5) {
                    binding.root.strokeWidth = 0
                    chosenCover = null
                    listener?.onCoverChosen(Uri.EMPTY)
                } else {
                    chosenCover?.strokeWidth = 0
                    binding.root.strokeWidth = 5
                    chosenCover = binding.root
                    listener?.onCoverChosen(uri)
                }
            }
        }
    }

    fun updateChosen(uri: Uri) {
        chosenCover?.strokeWidth = 0
        chosenCoverUri = uri
        currentList.forEachIndexed { index, item ->
            if (item == uri) {
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverViewHolder {
        val binding = ItemCoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoverViewHolder, position: Int) {
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