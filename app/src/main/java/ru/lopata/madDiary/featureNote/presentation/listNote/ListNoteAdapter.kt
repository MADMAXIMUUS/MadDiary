package ru.lopata.madDiary.featureNote.presentation.listNote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemNoteBinding
import ru.lopata.madDiary.featureNote.domain.model.entity.Note

class ListNoteAdapter(
    val onNoteClickListener: (note: Note) -> Unit
) : ListAdapter<Note, ListNoteAdapter.NoteViewHolder>(DiffCallback()) {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.apply {
                itemNoteContent.text = note.text
                itemNoteTitle.text = note.title
                //itemNoteDate.text = note.timestamp
                if (note.pinned)
                    itemNotePinImg.visibility = View.VISIBLE
                else
                    itemNotePinImg.visibility = View.GONE

                root.setOnClickListener {
                    onNoteClickListener(note)
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.noteId == newItem.noteId

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