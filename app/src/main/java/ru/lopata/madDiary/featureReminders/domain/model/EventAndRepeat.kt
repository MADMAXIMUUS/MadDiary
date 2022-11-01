package ru.lopata.madDiary.featureReminders.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class EventAndRepeat(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventOwnerId"
    )
    val repeat: Repeat?
)
