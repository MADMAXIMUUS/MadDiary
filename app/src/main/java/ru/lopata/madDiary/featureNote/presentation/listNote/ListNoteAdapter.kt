package ru.lopata.madDiary.featureNote.presentation.listNote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.lopata.madDiary.databinding.ItemNoteBinding
import ru.lopata.madDiary.featureNote.domain.model.NoteItem
import ru.lopata.madDiary.featureReminders.presentation.listEvents.adapters.DelegateAdapter
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem

class ListNoteAdapter(
    private val listener: OnItemClick
) : DelegateAdapter<NoteItem, ListNoteAdapter.NoteViewHolder>(NoteItem::class.java) {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteItem) {
            binding.apply {
                itemNoteContent.text = note.text
                itemNoteTitle.text = note.title
                itemNoteDate.text = note.timestamp
                if (note.pinned)
                    itemNotePinImg.visibility = View.VISIBLE
                else
                    itemNotePinImg.visibility = View.GONE

                root.setOnClickListener {
                    listener.onItemClick(note)
                }
            }
        }

    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoteViewHolder(binding)
    }

    override fun bindViewHolder(
        model: NoteItem,
        viewHolder: NoteViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        viewHolder.bind(model)
    }

    interface OnItemClick {
        fun onItemClick(note: NoteItem)
    }
}