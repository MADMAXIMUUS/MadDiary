package ru.madmax.madnotes.domain.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    val title: String = "",
    val iconTint: Int = -1,
    val color: Int = -1,

    @PrimaryKey val categoryId: Int? = null
)
