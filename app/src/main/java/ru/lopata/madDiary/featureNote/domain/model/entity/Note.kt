package ru.lopata.madDiary.featureNote.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NOTES")
data class Note(
    val title: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val pinned: Boolean = false,
    val color: Int = -1,

    @PrimaryKey val noteId: Int? = null
)
