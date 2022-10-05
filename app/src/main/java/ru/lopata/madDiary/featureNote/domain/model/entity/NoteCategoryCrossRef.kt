package ru.lopata.madDiary.featureNote.domain.model.entity

import androidx.room.Entity

@Entity(primaryKeys = ["noteId", "categoryId"])
data class NoteCategoryCrossRef(
    val noteId: Int = -1,
    val categoryId: Int = -1
)