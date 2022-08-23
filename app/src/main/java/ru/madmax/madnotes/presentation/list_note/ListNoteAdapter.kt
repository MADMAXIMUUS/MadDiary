package ru.madmax.madnotes.presentation.list_note

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.madmax.madnotes.databinding.ItemNoteBinding
import ru.madmax.madnotes.domain.model.Note
import ru.madmax.madnotes.util.NoteColors

class ListNoteAdapter(
    val onNoteClickListener: (note: Note) -> Unit
) : ListAdapter<Note, ListNoteAdapter.NoteViewHolder>(DiffCallback()) {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, onClickListener: (note: Note) -> Unit) {
            binding.apply {
                itemNoteRoot.setCardBackgroundColor(note.color)
                itemNoteCategories.text = note.categories
                itemNoteContent.text = note.text
                itemNoteTitle.text = note.title
                itemNoteDate.text = note.timestamp.toString()
                if (note.pinned)
                    itemNotePinImg.visibility = View.VISIBLE
                else
                    itemNotePinImg.visibility = View.GONE

                if (note.color == NoteColors.defaultDark){
                    itemNoteCategories.setTextColor(Color.parseColor("#FFD4D4D4"))
                    itemNoteContent.setTextColor(Color.parseColor("#FFD4D4D4"))
                    itemNoteTitle.setTextColor(Color.parseColor("#FFE3E9E5"))
                    itemNoteDate.setTextColor(Color.parseColor("#FFD4D4D4"))
                    itemNotePinImg.setColorFilter(Color.parseColor("#FFD4D4D4"))
                }

                root.setOnClickListener {
                    onClickListener(note)
                }
            }
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
        holder.bind(currentItem) { note ->
            onNoteClickListener(note)
        }
    }
}