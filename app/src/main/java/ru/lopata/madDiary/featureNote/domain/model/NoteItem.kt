package ru.lopata.madDiary.featureNote.domain.model

import ru.lopata.madDiary.featureNote.domain.model.entity.Category
import ru.lopata.madDiary.featureReminders.presentation.listEvents.itemStates.DelegateAdapterItem

data class NoteItem(
    val noteId: Int = -1,
    val title: String = "",
    val text: String = "",
    val timestamp: String = "",
    val pinned: Boolean = false,
    val color: Int = -1,
    val categories: List<Category>
) : DelegateAdapterItem {

    override fun id(): Any {
        return noteId
    }

    override fun content(): Any {
        return text
    }
}
