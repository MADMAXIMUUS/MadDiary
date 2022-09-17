package ru.madmax.madDiary.featureNote.presentation.listNote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.madmax.madDiary.databinding.ItemNoteBinding
import ru.madmax.madDiary.featureNote.domain.model.entity.NoteModel

class ListNoteAdapter(
    val onNoteClickListener: (note: NoteModel) -> Unit
) : ListAdapter<NoteModel, ListNoteAdapter.NoteViewHolder>(DiffCallback()) {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteModel) {
            binding.apply {
                itemNoteRoot.setCardBackgroundColor(note.color)
                itemNoteContent.text = note.text
                itemNoteTitle.text = note.title
                itemNoteDate.text = note.timestamp
                if (note.pinned)
                    itemNotePinImg.visibility = View.VISIBLE
                else
                    itemNotePinImg.visibility = View.GONE

                /*if (note.color == NoteColors.defaultDark) {
                    itemNoteCategories.setTextColor(Color.parseColor("#FFD4D4D4"))
                    itemNoteContent.setTextColor(Color.parseColor("#FFD4D4D4"))
                    itemNoteTitle.setTextColor(Color.parseColor("#FFE3E9E5"))
                    itemNoteDate.setTextColor(Color.parseColor("#FFD4D4D4"))
                    itemNotePinImg.setColorFilter(Color.parseColor("#FFD4D4D4"))
                }*/

                root.setOnClickListener {
                    onNoteClickListener(note)
                }
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel) = oldItem == newItem
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