package ru.lopata.madDiary.featureNote.domain.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "NOTES")
@Parcelize
data class Note(
    val title: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val pinned: Boolean = false,
    val color: Int = -1,

    @PrimaryKey val noteId: Int? = null
) : Parcelable
