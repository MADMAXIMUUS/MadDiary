package ru.madmax.madnotes.domain.model

import android.graphics.Color
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "NOTES")
data class Note(
    val title: String,
    val text: String,
    val categories: String,
    val timestamp: Long,
    val pinned: Boolean = false,
    val color: Int = Color.parseColor("#FFF5F5F5"),

    @PrimaryKey val id: Int? = null
): Parcelable
