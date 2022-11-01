package ru.lopata.madDiary.featureReminders.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "REPEATS")
data class Repeat(
    val repeatStart: Date = Date(0),
    val repeatInterval: Long = NO_REPEAT,
    val repeatEnd: Date = Date(0),
    val eventOwnerId: Int = 0,

    @PrimaryKey val repeatId: Int? = null
) {
    companion object {
        const val NO_REPEAT: Long = 0
        const val EVERY_DAY: Long = 86400000
        const val EVERY_SECOND_DAY: Long = EVERY_DAY * 2
        const val EVERY_WEEK: Long = EVERY_DAY * 7
        const val EVERY_SECOND_WEEK: Long = EVERY_WEEK * 2
        const val EVERY_MONTH: Long = EVERY_DAY * 30
        const val EVERY_YEAR: Long = EVERY_DAY * 365
    }
}
