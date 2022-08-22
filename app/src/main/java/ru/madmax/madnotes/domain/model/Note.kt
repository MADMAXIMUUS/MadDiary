package ru.madmax.madnotes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.madmax.madnotes.util.NoteColors

@Entity(tableName = "NOTES")
data class Note(
    val title: String = "",
    val text: String = "",
    val categories: String = "",
    val timestamp: Long = 0,
    val pinned: Boolean = false,
    val color: Int = NoteColors.default,

    @PrimaryKey val id: Int? = null
)
