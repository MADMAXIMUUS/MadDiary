package ru.madmax.madDiary.featureNote.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "NOTES")
data class Note(
    val title: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val pinned: Boolean = false,
    val color: Int = -1,

    @PrimaryKey val noteId: Int? = null
) {
    fun toNoteModel(categories: List<Category>): NoteModel {
        val simpleDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val date = simpleDate.format(calendar.time)

        return NoteModel(
            title = title,
            text = text,
            timestamp = date,
            categories = categories,
            pinned = pinned,
            color = color,
            id = noteId
        )
    }
}

data class NoteModel(
    val title: String = "",
    val text: String = "",
    val timestamp: String = "",
    val categories: List<Category>,
    val pinned: Boolean = false,
    val color: Int = -1,
    val id: Int? = null
)
