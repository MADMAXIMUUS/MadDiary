package ru.madmax.maddiary.feature_note.domain.model.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.madmax.maddiary.feature_note.domain.model.entity.Category
import ru.madmax.maddiary.feature_note.domain.model.entity.Note
import ru.madmax.maddiary.feature_note.domain.model.entity.NoteCategoryCrossRef

data class CategoryWithNotes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "noteId",
        associateBy = Junction(NoteCategoryCrossRef::class)
    ) val Notes: List<Note>
)