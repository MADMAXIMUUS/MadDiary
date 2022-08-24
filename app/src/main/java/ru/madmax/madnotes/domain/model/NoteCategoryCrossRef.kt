package ru.madmax.madnotes.domain.model

import androidx.room.Entity

@Entity(primaryKeys = ["noteId", "categoryId"])
data class NoteCategoryCrossRef(
    val noteId: Long,
    val categoryId: Long
)