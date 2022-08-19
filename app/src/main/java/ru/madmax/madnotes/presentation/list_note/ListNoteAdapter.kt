package ru.madmax.madnotes.presentation.list_note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.madmax.madnotes.databinding.ItemNoteBinding
import ru.madmax.madnotes.domain.model.Note

class ListNoteAdapter : ListAdapter<Note, ListNoteAdapter.NoteViewHolder>(DiffCallback()) {

    class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.itemNoteRoot.setBackgroundColor(note.color)
            binding.itemNoteCategories.text = note.categories
            binding.itemNoteContent.text = note.text
            binding.itemNoteTitle.text = note.title
            binding.itemNoteDate.text = note.timestamp.toString()
            if (note.pinned)
                binding.itemNotePinImg.visibility = View.VISIBLE
            else
                binding.itemNotePinImg.visibility = View.GONE
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}