package ru.madmax.madnotes.domain.model.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.madmax.madnotes.domain.model.entity.Category
import ru.madmax.madnotes.domain.model.entity.Note
import ru.madmax.madnotes.domain.model.entity.NoteCategoryCrossRef

data class NoteWithCategories(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "categoryId",
        associateBy = Junction(NoteCategoryCrossRef::class)
    ) val categories: List<Category>
)