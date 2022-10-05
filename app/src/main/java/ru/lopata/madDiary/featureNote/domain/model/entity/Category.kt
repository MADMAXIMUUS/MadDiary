package ru.lopata.madDiary.featureNote.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CATEGORIES")
data class Category(
    val title: String = "",
    val iconTint: Int = -1,
    val color: Int = -1,

    @PrimaryKey(autoGenerate = true) val categoryId: Int? = null
)
