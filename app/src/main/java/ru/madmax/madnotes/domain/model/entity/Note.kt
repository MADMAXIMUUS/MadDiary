package ru.madmax.madnotes.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.madmax.madnotes.util.NoteColors

@Entity(tableName = "NOTES")
data class Note(
    val title: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val pinned: Boolean = false,
    val color: Int = -1,

    @PrimaryKey val id: Int? = null
) {
    fun toNoteModel(categories: List<Category>): NoteModel {
        return NoteModel(
            title = title,
            text = text,
            timestamp = timestamp,
            categories = categories,
            pinned = pinned,
            color = color,
            id = id
        )
    }
}

data class NoteModel(
    val title: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val categories: List<Category>,
    val pinned: Boolean = false,
    val color: Int = -1,
    val id: Int? = null
)
