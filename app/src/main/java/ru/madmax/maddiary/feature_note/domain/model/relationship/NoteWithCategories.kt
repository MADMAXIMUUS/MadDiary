package ru.madmax.maddiary.feature_note.domain.model.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.madmax.maddiary.feature_note.domain.model.entity.Category
import ru.madmax.maddiary.feature_note.domain.model.entity.Note
import ru.madmax.maddiary.feature_note.domain.model.entity.NoteCategoryCrossRef

data class NoteWithCategories(
    @Embedded val note: Note = Note(),
    @Relation(
        parentColumn = "noteId",
        entityColumn = "categoryId",
        associateBy = Junction(NoteCategoryCrossRef::class)
    ) val categories: List<Category> = emptyList()
)