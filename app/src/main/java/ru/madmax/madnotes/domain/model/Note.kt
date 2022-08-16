package ru.madmax.madnotes.domain.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NOTES")
data class Note(
    val title: String,
    val text: String,
    val categories: String,
    val timestamp: Long,
    val pinned: Boolean = false,
    val color: Int = noteColors[0],

    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(
            Color.parseColor("#FFF5F5F5"),
            Color.parseColor("#FFE8D0FF"),
            Color.parseColor("#FFCFFFC8"),
            Color.parseColor("#FFFFF599"),
            Color.parseColor("#FFD0C9FF"),
            Color.parseColor("#FFC1D9FF")
        )
    }
}
